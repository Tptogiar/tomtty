package com.tptogiar.network.nio.eventloop;

import com.tptogiar.network.nio.poller.MianPoller;
import com.tptogiar.network.nio.poller.Poller;
import com.tptogiar.network.nio.poller.SubPoller;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月26日 15:49:00
 */
@Data
public class NioEnventLoop extends Thread{

    private static final Logger logger = LoggerFactory.getLogger(NioEnventLoop.class);

    private NioEventLoopGroup eventLoopGroup;

    private final Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private final Poller poller;

    private int subReactorIndex = 0;

    private volatile boolean canSelect = true;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String name;

    private static final String  EVENT_LOOP_NAME_MAIN = "MainReactor";

    private static final String  EVENT_LOOP_NAME_SUB = "SubReactor";




    private NioEnventLoop(int port, NioEventLoopGroup eventLoopGroup,String name) throws IOException {
        this.eventLoopGroup = eventLoopGroup;
        this.name = name;
        serverSocketChannel = initServerSocket(port);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        poller = new MianPoller(this);

    }




    private NioEnventLoop() throws IOException {
        this.selector = Selector.open();
        this.poller = new SubPoller(this);
    }

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




    @SneakyThrows
    @Override
    public void run() {
        poller.poll();
    }




    public void select() throws IOException {
        logger.info(this+"执行select...");
        selector.select();
    }




    public void dispatcherToSubReactor(SocketChannel clientChannel) throws IOException {
        NioEnventLoop subEventLoop = eventLoopGroup.getEventLoop();
        logger.info(String.valueOf(clientChannel.getRemoteAddress())+"分配到了"+subEventLoop);
        registerToSubEventLoop(clientChannel,subEventLoop);
    }




    public void registerToSubEventLoop(SocketChannel clientChannel, NioEnventLoop subEventLoop) throws ClosedChannelException {
        Selector subSelector = subEventLoop.getSelector();
        // 为防止channel的register方法与subSelector的select发生死锁，这里
        // 将canSelect标识置为false以防止subSelector重新陷入select阻塞
        canSelect = false;
        subEventLoop.selectorWakeup();
        clientChannel.register(subSelector,SelectionKey.OP_READ);
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
