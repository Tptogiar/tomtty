package com.tptogiar.network.nio.handler;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 23:36:00
 */
public class NioHttpHandler implements Runnable{

    private TCPHandler tcpHandler;

    public NioHttpHandler(TCPHandler tcpHandler, ByteBuffer buffer) {
        this.tcpHandler = tcpHandler;
    }

    @Override
    public void run() {

    }
}
