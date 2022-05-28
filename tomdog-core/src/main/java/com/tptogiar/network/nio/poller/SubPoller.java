package com.tptogiar.network.nio.poller;

import com.tptogiar.network.nio.handler.TCPHandler;
import com.tptogiar.network.nio.reactor.NioEnventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 22:21:00
 */
public class SubPoller implements Poller {


    Logger logger = LoggerFactory.getLogger(SubPoller.class);

    private NioEnventLoop nioEnventLoop;

    private Selector selector;

    private TCPHandler tcpHandler;

    public SubPoller(NioEnventLoop nioEnventLoop) {
        this.nioEnventLoop = nioEnventLoop;

        selector = nioEnventLoop.getSelector();
        tcpHandler = new TCPHandler();
    }


    @Override
    public void poll() throws IOException {
        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (!selectionKey.isValid()) {
                    continue;
                }

                if (selectionKey.isReadable()) {
                    tcpHandler.read(selectionKey);
                }
                else if (selectionKey.isWritable()){
                    tcpHandler.write(selectionKey);
                }


            }
        }
    }
}
