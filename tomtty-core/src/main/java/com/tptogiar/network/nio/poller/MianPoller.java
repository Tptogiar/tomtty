package com.tptogiar.network.nio.poller;


import com.tptogiar.network.nio.eventloop.NioEventLoop;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月26日 15:58:00
 */
@Data
public class MianPoller implements Poller {


    Logger logger = LoggerFactory.getLogger(MianPoller.class);


    private NioEventLoop nioEnventLoop;

    private AtomicBoolean running;


    public MianPoller(NioEventLoop nioEnventLoop) {

        this.nioEnventLoop = nioEnventLoop;
        running = nioEnventLoop.getRunning();
    }


    @Override
    public void poll() throws IOException {

        while (running.get()) {

            scanSelectionKeys();

        }

    }


    private void scanSelectionKeys() throws IOException {

        int select = nioEnventLoop.select();
        Set<SelectionKey> selectionKeys = nioEnventLoop.getSelector().selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
            iterator.remove();
            if (!selectionKey.isValid()) {
                continue;
            }
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = nioEnventLoop.getServerSocketChannel();
                SocketChannel clientChannel = serverSocketChannel.accept();
                clientChannel.configureBlocking(false);
                nioEnventLoop.dispatcherToSubReactor(clientChannel);
            }

        }
    }


}
