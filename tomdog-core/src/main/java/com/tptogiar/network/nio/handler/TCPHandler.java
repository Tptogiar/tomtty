package com.tptogiar.network.nio.handler;

import com.tptogiar.component.pool.Pool;
import com.tptogiar.network.bio.handler.ProcessResult;
import com.tptogiar.network.nio.eventloop.NioEnventLoop;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
        if (readCount == -1 || readCount == 0) {
            subEventLoop.closeClientChannel(channel);
            return;
        }
        logger.info("读取通道内数据读取完毕，大小为{}...", buffer.limit());
        logger.debug("\n" + new String(buffer.array(), 0, buffer.limit()) + "\n");
        // 将读取的数据交给业务线程处理
        NioHttpHandler nioHttpHandler = new NioHttpHandler(buffer, selectionKey, subEventLoop);
        Pool.execute(nioHttpHandler);

    }


    public void write(SelectionKey selectionKey) throws IOException {
        logger.info("处理写事件...");
        ProcessResult processResult = (ProcessResult) selectionKey.attachment();
        SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
        if (processResult.isFileTransfer()){
            writeResponseFromFile(processResult,clientChannel);
        }else {
            writeResponse(processResult,clientChannel);
        }
        logger.info("在Selector:{}上取消事件注册...", selectionKey.selector().hashCode());
        clientChannel.shutdownInput();
        clientChannel.close();

    }


    public static void writeResponseFromFile(ProcessResult processResult,
                                             SocketChannel clientChannel) throws IOException {

        // TODO 从源头转为byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(processResult.getResponseHeaderBytes());
        clientChannel.write(byteBuffer);
        FileChannel srcFileChannel = processResult.getSrcFileChannel();
        // 使用零拷贝传输该静态资源文件
        srcFileChannel.transferTo(0,srcFileChannel.size(),clientChannel);

    }

    public static void writeResponse(ProcessResult processResult,
                                     SocketChannel clientChannel) throws IOException {

        byte[] responseBytes = processResult.getResponseBytes();
        // TODO 从源头转为byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(responseBytes);
        clientChannel.write(byteBuffer);
    }


}
