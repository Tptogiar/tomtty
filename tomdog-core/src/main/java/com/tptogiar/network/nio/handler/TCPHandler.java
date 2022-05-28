package com.tptogiar.network.nio.handler;

import com.tptogiar.component.pool.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    private static final Logger logger = LoggerFactory.getLogger(TCPHandler.class);

    public void read(SelectionKey selectionKey) throws IOException {
        logger.info("开始读取通道内数据...");
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        // TODO 1024?
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();
        logger.debug(new String(buffer.array(),0,buffer.limit()));
        // 将读取的数据交给业务线程处理
        NioHttpHandler nioHttpHandler = new NioHttpHandler(this, buffer);
        Pool.submit(nioHttpHandler);

    }


    public void write(SelectionKey selectionKey){

    }





}
