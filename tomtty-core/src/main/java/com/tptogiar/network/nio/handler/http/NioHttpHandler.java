package com.tptogiar.network.nio.handler.http;

import com.tptogiar.network.HttpHandler;
import com.tptogiar.network.bio.handler.ProcessResult;
import com.tptogiar.network.nio.eventloop.NioEventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * 业务worker
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 23:36:00
 */
public class NioHttpHandler extends HttpHandler implements Runnable {

    private Logger logger = LoggerFactory.getLogger(NioHttpHandler.class);

    private ByteBuffer buffer;

    private SelectionKey selectionKey;

    private NioEventLoop subEventLoop;


    public NioHttpHandler(ByteBuffer buffer, SelectionKey selectionKey, NioEventLoop subEventLoop) {
        this.selectionKey = selectionKey;
        this.buffer = buffer;
        this.subEventLoop = subEventLoop;
    }


    @Override
    public void run() {

        logger.info("由{}提交的处理器开始处理请求...", subEventLoop);

        ProcessResult processResult = null;
        try {
            processResult = process(buffer.array());
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            subEventLoop.registerEvent2SelectorTaskQueue(channel, subEventLoop, SelectionKey.OP_WRITE, processResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("由{}提交的处理器请求处理完毕...", subEventLoop);
    }
}
