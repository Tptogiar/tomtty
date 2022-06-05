package com.tptogiar.network.nio.eventloop;

import com.tptogiar.component.connection.ConnectionMgr;
import com.tptogiar.network.nio.connection.Connection;
import com.tptogiar.network.nio.poller.MianPoller;
import com.tptogiar.network.nio.poller.Poller;
import com.tptogiar.network.nio.poller.SubPoller;
import com.tptogiar.network.nio.task.EventTask;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Tptogiar
 * @description
 * @date 2022/6/1 - 17:17
 */
@Data
public class NioEventLoop extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(NioEventLoop.class);


    private NioEventLoopGroup eventLoopGroup;

    // 任务队列
    private BlockingQueue<EventTask> eventQueue;

    // 连接管理
    private ConnectionMgr connectionMgr;

    private final Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private final Poller poller;

    private int subReactorIndex = 0;

    private AtomicBoolean selecting = new AtomicBoolean(false);

    private AtomicBoolean running = new AtomicBoolean(true);

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String name;

    private static final String EVENT_LOOP_NAME_MAIN = "MainReactor";

    private static final String EVENT_LOOP_NAME_SUB = "SubReactor";


    /**
     * 构造器，用于构造MainReactor
     *
     * @param port
     * @param eventLoopGroup
     * @param name
     * @throws IOException
     */
    private NioEventLoop(int port, NioEventLoopGroup eventLoopGroup, String name) throws IOException {
        this.eventLoopGroup = eventLoopGroup;
        this.name = name;
        serverSocketChannel = initServerSocket(port);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        poller = new MianPoller(this);

    }


    /**
     * 构造器，用于构造SubReactor
     *
     * @param index
     * @param name
     * @throws IOException
     */
    public NioEventLoop(int index, String name) throws IOException {
        selector = Selector.open();
        eventQueue = new LinkedBlockingQueue<>();
        connectionMgr = new ConnectionMgr(this);
        poller = new SubPoller(this);
        this.subReactorIndex = index;
        this.name = name;

    }


    public static NioEventLoop createSubEventLoop(int index) throws IOException {
        return new NioEventLoop(index, EVENT_LOOP_NAME_SUB);
    }


    public static NioEventLoop createMainEventLoop(int port, NioEventLoopGroup eventLoopGroup) throws IOException {
        logger.info("初始化MainEventLoop...");
        return new NioEventLoop(port, eventLoopGroup, EVENT_LOOP_NAME_MAIN);
    }


    private static ServerSocketChannel initServerSocket(int port) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(port));
        logger.info("监听端口号为：" + port + " ...");
        return channel;
    }


    @Override
    public void run() {
        try {
            poller.poll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info(this + "将要停止运行...");
    }


    public int select() throws IOException {
        return selector.select();
    }


    public void dispatcherToSubReactor(SocketChannel clientChannel) throws IOException {
        NioEventLoop subEventLoop = eventLoopGroup.getEventLoop();
        logger.info(String.valueOf(clientChannel.getRemoteAddress()) + "分配到的subReactor：" + subEventLoop);
        registerEvent2SelectorTaskQueue(clientChannel, subEventLoop, SelectionKey.OP_READ, null);
    }


    public void registerEvent2SelectorTaskQueue(SocketChannel clientChannel, NioEventLoop curEventLoop, int ops, Object attchment) {

        Selector curSelector = curEventLoop.getSelector();
        BlockingQueue<EventTask> eventQueue = curEventLoop.getEventQueue();
        addEventTask(curSelector, eventQueue, () -> {

            try {
                if (attchment == null) {
                    // 主MainReactor将accept后的selectionKey注册到SubReactor
                    clientChannel.register(curSelector, ops);
                } else {
                    // SubReactor内的NioHttpHandler注册写事件等
                    clientChannel.register(curSelector, ops, attchment);
                }

            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        });

        selectorWakeupIfSelecting(curEventLoop);
    }

    /**
     * 将关闭客户端连接的动作延迟到下一次处理任务时
     *
     * @param channel
     */
    public void closeClientChannel(SocketChannel channel) {
        addEventTask(selector, eventQueue, () -> {
            try {
                logger.info("关闭与{}的连接", channel.getRemoteAddress());
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        selectorWakeupIfSelecting(this);
    }


    /**
     * 把任务添加进阻塞队列
     *
     * @param selector
     * @param eventQueue
     * @param runnable
     */
    public void addEventTask(Selector selector, BlockingQueue<EventTask> eventQueue, Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("the evnet runnable must not be null");
        }
        if (!selector.isOpen()) {
            return;
        }
        eventQueue.add(new EventTask(runnable));
    }


    private void selectorWakeupIfSelecting(NioEventLoop curEventLoop) {
        // 如果正在进行select，则考虑把其唤醒
        if (curEventLoop.getSelecting().get()) {
            curEventLoop.getSelector().wakeup();
        }
    }


    @Override
    public String toString() {
        if (EVENT_LOOP_NAME_MAIN.equals(name)) {
            return "{MainReactor}";
        } else {
            return "{SubReactor_" + subReactorIndex + "}";
        }
    }

    /**
     * 处理超时的连接
     *
     * @param connection
     */
    public void handleExpiredConnection(Connection connection) {
        SelectionKey selectionKey = connection.getSelectionKey();
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        closeClientChannel(channel);
    }


}
