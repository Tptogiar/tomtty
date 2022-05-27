package com.tptogiar.network.nio.reactor;

import com.tptogiar.network.nio.poller.MianPoller;
import com.tptogiar.network.nio.poller.Poller;
import com.tptogiar.network.nio.poller.SubPoller;
import lombok.Data;
import lombok.SneakyThrows;
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

    private static Logger logger = LoggerFactory.getLogger(NioEnventLoop.class);

    private NioEventLoopGroup eventLoopGroup;

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private Poller poller;

    private int subReactorIndex = 0;





    private NioEnventLoop(int port, NioEventLoopGroup eventLoopGroup) throws IOException {

        this.eventLoopGroup = eventLoopGroup;
        serverSocketChannel = initServerSocket(port);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        poller = new MianPoller(this);

    }




    private NioEnventLoop() throws IOException {
        this.selector = Selector.open();
        this.poller = new SubPoller(this);
    }




    public static NioEnventLoop createSubEventLoop() throws IOException {
        return new NioEnventLoop();
    }




    public static NioEnventLoop createMainEventLoop(int port, NioEventLoopGroup eventLoopGroup) throws IOException {
        return new NioEnventLoop(port, eventLoopGroup);
    }




    private static ServerSocketChannel initServerSocket(int port) throws IOException {
        ServerSocketChannel channel = null;

        channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(port));

        return channel;
    }




    @SneakyThrows
    @Override
    public void run() {
        poller.poll();
    }




    public void select() throws IOException {
        selector.select(1000);

    }




    public void dispatcherToSubReactor(SocketChannel clientChannel) throws ClosedChannelException {
        NioEnventLoop subEventLoop = eventLoopGroup.getEventLoop();
        registerToSubEventLoop(clientChannel,subEventLoop);
    }




    public void registerToSubEventLoop(SocketChannel clientChannel, NioEnventLoop subEventLoop) throws ClosedChannelException {
        Selector subSelector = subEventLoop.getSelector();
        clientChannel.register(subSelector,SelectionKey.OP_READ);
    }


}
