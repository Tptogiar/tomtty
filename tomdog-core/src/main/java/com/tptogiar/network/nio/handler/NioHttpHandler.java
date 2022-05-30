package com.tptogiar.network.nio.handler;

import com.tptogiar.network.HttpHandler;
import com.tptogiar.network.nio.eventloop.NioEnventLoop;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 23:36:00
 */
public class NioHttpHandler extends HttpHandler implements Runnable {

    private ByteBuffer buffer;

    private SelectionKey selectionKey;

    private NioEnventLoop subEventLoop;

    private Selector subSelector;

    public NioHttpHandler(ByteBuffer buffer, SelectionKey selectionKey, NioEnventLoop subEventLoop) {
        this.selectionKey = selectionKey;
        this.buffer = buffer;
        this.subEventLoop = subEventLoop;
        this.subSelector = subSelector;
    }


    @Override
    public void run() {
        byte[] responseBytes = null;
        try {
            responseBytes = process(buffer.array());
//            selectionKey.channel().register(subSelector, SelectionKey.OP_WRITE);
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            subEventLoop.registerWriteToSelector(channel);
            selectionKey.attach(responseBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
