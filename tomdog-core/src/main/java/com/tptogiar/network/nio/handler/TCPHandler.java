package com.tptogiar.network.nio.handler;

import com.tptogiar.component.pool.Pool;
import com.tptogiar.network.nio.eventloop.NioEnventLoop;
import com.tptogiar.network.nio.eventloop.NioEventLoopGroup;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 23:09:00
 */
@Data
public class TCPHandler {


    private static final Logger logger = LoggerFactory.getLogger(TCPHandler.class);

    private NioEnventLoop subEventLoop;

    private Selector subSelector;


    public TCPHandler(NioEnventLoop subEventLoop) {
        this.subEventLoop = subEventLoop;
        this.subSelector = subEventLoop.getSelector();

    }

    public void read(SelectionKey selectionKey) throws IOException {
        logger.info("开始读取通道内数据...");
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        // TODO 1024?
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int readCount = channel.read(buffer);
        buffer.flip();
        if (readCount == -1 || readCount ==0){
            // TODO 关闭连接？
            channel.shutdownInput();
            channel.close();
            subEventLoop.getSelector().select(10);
            boolean registered = channel.isRegistered();
            return;
        }
        logger.debug("\n"+new String(buffer.array(),0,buffer.limit())+"\n");
        // 将读取的数据交给业务线程处理
        NioHttpHandler nioHttpHandler = new NioHttpHandler( buffer,selectionKey, subEventLoop);
//        Pool.submit(nioHttpHandler);
        Pool.execute(nioHttpHandler);

    }


    public void write(SelectionKey selectionKey) throws IOException {
        logger.info("处理写事件...");
        byte[] responseBytes = (byte[]) selectionKey.attachment();
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        // TODO 从源头转为byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(responseBytes);
        channel.write(byteBuffer);
        logger.info("在Selector:{}上取消事件注册...",selectionKey.selector().hashCode());
        selectionKey.cancel();
        channel.shutdownInput();
        channel.close();
        boolean registered = channel.isRegistered();
    }





}
