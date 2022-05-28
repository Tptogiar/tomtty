package com.tptogiar.network.nio.poller;

import com.tptogiar.network.nio.reactor.NioEnventLoop;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月26日 15:58:00
 */
@Data
public class MianPoller implements Poller {

    Logger logger = LoggerFactory.getLogger(MianPoller.class);


    private NioEnventLoop nioEnventLoop;



    public MianPoller(NioEnventLoop nioEnventLoop) {
        this.nioEnventLoop = nioEnventLoop;
    }



    @Override
    public void poll() throws IOException {
        while (true) {
            nioEnventLoop.select();
            Set<SelectionKey> selectionKeys = nioEnventLoop.getSelector().selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (! selectionKey.isValid()){
                    continue;
                }
                if (selectionKey.isAcceptable()){
                    ServerSocketChannel serverSocketChannel = nioEnventLoop.getServerSocketChannel();
                    SocketChannel clientChannel = serverSocketChannel.accept();
                    nioEnventLoop.dispatcherToSubReactor(clientChannel);
                }

            }


        }

    }
}
