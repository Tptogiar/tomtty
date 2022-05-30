package com.tptogiar.network.nio.poller;

import com.tptogiar.network.nio.handler.TCPHandler;
import com.tptogiar.network.nio.eventloop.NioEnventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 22:21:00
 */
public class SubPoller implements Poller {

    Logger logger = LoggerFactory.getLogger(SubPoller.class);

    private NioEnventLoop subEventLoop;

    private Selector subSelector;

    private TCPHandler tcpHandler;

    public SubPoller(NioEnventLoop subEventLoop) {
        this.subEventLoop = subEventLoop;
        subSelector = subEventLoop.getSelector();
        tcpHandler = new TCPHandler(subEventLoop);
    }


    @Override
    public void poll() throws IOException {
        while (true) {
            // 当MainReactor不在操作selector是才进行select
            if (! subEventLoop.getCanSelect().get()) {
                continue;
            }

            int select = subEventLoop.select();
            Set<SelectionKey> selectionKeys = subSelector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (!selectionKey.isValid()) {
                    continue;
                }

                if (selectionKey.isReadable()) {
                    tcpHandler.read(selectionKey);
                } else if (selectionKey.isWritable()) {
                    tcpHandler.write(selectionKey);
                }


            }
        }
    }
}
