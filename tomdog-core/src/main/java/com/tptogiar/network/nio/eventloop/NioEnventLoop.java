package com.tptogiar.network.nio.eventloop;

import com.tptogiar.network.nio.poller.MianPoller;
import com.tptogiar.network.nio.poller.Poller;
import com.tptogiar.network.nio.poller.SubPoller;
import com.tptogiar.network.nio.task.EventTask;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月26日 15:49:00
 */
@Data
public class NioEnventLoop extends Thread{

    private static final Logger logger = LoggerFactory.getLogger(NioEnventLoop.class);


    private NioEventLoopGroup eventLoopGroup;

    // 任务队列
    private BlockingQueue<EventTask> eventQueue = new LinkedBlockingQueue<>();

    private final Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private final Poller poller;

    private int subReactorIndex = 0;

    private AtomicBoolean selecting = new AtomicBoolean(false);

    private AtomicBoolean running = new AtomicBoolean(true);

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String name;

    private static final String  EVENT_LOOP_NAME_MAIN = "MainReactor";

    private static final String  EVENT_LOOP_NAME_SUB = "SubReactor";


    /**
     * 构造器，用于构造MainReactor
     * @param port
     * @param eventLoopGroup
     * @param name
     * @throws IOException
     */
    private NioEnventLoop(int port, NioEventLoopGroup eventLoopGroup,String name) throws IOException {
        this.eventLoopGroup = eventLoopGroup;
        this.name = name;
        serverSocketChannel = initServerSocket(port);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        poller = new MianPoller(this);

    }


    /**
     * 构造器，用于构造SubReactor
     * @param index
     * @param name
     * @throws IOException
     */
    public NioEnventLoop(int index,String name) throws IOException {
        selector = Selector.open();
        poller = new SubPoller(this);
        this.subReactorIndex = index;
        this.name = name;
    }


    public static NioEnventLoop createSubEventLoop(int index) throws IOException {
        return new NioEnventLoop(index,EVENT_LOOP_NAME_SUB);
    }


    public static NioEnventLoop createMainEventLoop(int port, NioEventLoopGroup eventLoopGroup) throws IOException {
        logger.info("初始化MainEventLoop...");
        return new NioEnventLoop(port, eventLoopGroup,EVENT_LOOP_NAME_MAIN);
    }


    private static ServerSocketChannel initServerSocket(int port) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(port));
        logger.info("监听端口号为："+port+" ...");
        return channel;
    }


    @Override
    public void run() {
        try {
            poller.poll();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public int select() throws IOException {
        return selector.select();
    }


    public void dispatcherToSubReactor(SocketChannel clientChannel) throws IOException {
        NioEnventLoop subEventLoop = eventLoopGroup.getEventLoop();
        logger.info(String.valueOf(clientChannel.getRemoteAddress())+"分配到的subReactor："+subEventLoop);
        registerEventToSelector(clientChannel,subEventLoop, SelectionKey.OP_READ,null);
    }


    public void registerEventToSelector(SocketChannel clientChannel, NioEnventLoop curEventLoop, int ops,Object attchment) {

        Selector curSelector = curEventLoop.getSelector();
        BlockingQueue<EventTask> eventQueue = curEventLoop.getEventQueue();
        addEvent(curSelector,eventQueue,() ->{

            try {
                if (attchment==null){
                    clientChannel.register(curSelector,ops);
                }else{
                    clientChannel.register(curSelector,ops,attchment);
                }

            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        });

        // 如果正在进行select，则考虑把其唤醒
        // TODO 唤醒有代价，待考虑更好的方式
        if (curEventLoop.getSelecting().get()){
            curEventLoop.selectorWakeup();
        }
    }


    public void addEvent(Selector selector,BlockingQueue<EventTask> eventQueue,Runnable runnable){
        if (runnable == null){
            throw new NullPointerException("the evnet runnable must not be null");
        }
        if (! selector.isOpen()){
            return;
        }
        eventQueue.add(new EventTask(runnable));
    }


    private void selectorWakeup(){
        selector.wakeup();
    }


    @Override
    public String toString() {
        if (EVENT_LOOP_NAME_MAIN.equals(name)){
            return "{MainReactor}";
        }
        else{
            return "{SubReactor_"+subReactorIndex+"}";
        }
    }
}
