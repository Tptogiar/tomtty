package com.tptogiar.network.nio.handler;

import com.tptogiar.component.pool.Pool;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 23:09:00
 */
public class TCPHandler {






    public void read(SelectionKey selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        // TODO 1024?
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();
        // 将读取的数据交给业务线程处理
        NioHttpHandler nioHttpHandler = new NioHttpHandler(this, buffer);
        Pool.submit(nioHttpHandler);

    }


    public void write(SelectionKey selectionKey){

    }





}
