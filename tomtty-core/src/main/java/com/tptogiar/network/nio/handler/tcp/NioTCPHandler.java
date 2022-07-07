package com.tptogiar.network.nio.handler.tcp;

import com.tptogiar.component.connection.ConnectionMgr;
import com.tptogiar.component.pool.WorkerThreadPool;
import com.tptogiar.network.bio.handler.ProcessResult;
import com.tptogiar.network.nio.connection.Connection;
import com.tptogiar.network.nio.eventloop.NioEventLoop;
import com.tptogiar.network.nio.handler.http.NioHttpHandler;
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
 * 负责连接的读写
 *
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 23:09:00
 */
@Data
public class NioTCPHandler {


    private static final Logger logger = LoggerFactory.getLogger(NioTCPHandler.class);

    private NioEventLoop subEventLoop;

    private Selector subSelector;


    public NioTCPHandler(NioEventLoop subEventLoop) {

        this.subEventLoop = subEventLoop;
        this.subSelector = subEventLoop.getSelector();
    }


    public void read(SelectionKey selectionKey) throws IOException {

        logger.info("来自{} 的读事件,并添加到连接管理器...", ((SocketChannel) selectionKey.channel()).getRemoteAddress());
        Connection connection = ConnectionMgr.addConnection(selectionKey, subEventLoop);


        SocketChannel channel = (SocketChannel) selectionKey.channel();
        // TODO 1024?
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int readCount = channel.read(buffer);
        buffer.flip();


        if (readCount <= 0) {

            // 收到FIN的TCP包
            logger.info("收到FIN包TCP包,关闭连接...");

            // 把selectionKey从selector上取消注册
            selectionKey.cancel();

            handleConnectionClose(connection, channel);
            return;

        }
        logger.info("读取通道内数据读取完毕，大小为{}...", buffer.limit());
        logger.debug("\n" + new String(buffer.array(), 0, buffer.limit()) + "\n");


        // 将读取的数据交给业务线程处理
        NioHttpHandler nioHttpHandler = new NioHttpHandler(buffer, selectionKey, subEventLoop);
        WorkerThreadPool.execute(nioHttpHandler);

    }


    public void write(SelectionKey selectionKey) throws IOException {

        logger.info("来自{} 的写事件", ((SocketChannel) selectionKey.channel()).getRemoteAddress());
        ProcessResult processResult = (ProcessResult) selectionKey.attachment();
        SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
        selectionKey.isValid();
        if (processResult.isFileTransfer()) {
            writeResponseFromFile(processResult, clientChannel);
        } else {
            writeResponse(processResult, clientChannel);
        }


        // 请求头中未包含Connection: keep-alive时
        if (ConnectionMgr.contains(selectionKey)
                && processResult.isKeepAlive()
                && selectionKey.isValid()) {

            selectionKey.interestOps(SelectionKey.OP_READ);
            return;
        }

        logger.info("在Selector:{}上取消事件注册...", selectionKey.selector().hashCode());
        clientChannel.close();
    }


    public static void writeResponseFromFile(ProcessResult processResult,
                                             SocketChannel clientChannel) throws IOException {

        // TODO 从源头转为byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(processResult.getResponseHeaderBytes());
        clientChannel.write(byteBuffer);
        FileChannel srcFileChannel = processResult.getSrcFileChannel();
        // 使用零拷贝传输该静态资源文件
        long l = srcFileChannel.transferTo(0, srcFileChannel.size(), clientChannel);
    }


    public static void writeResponse(ProcessResult processResult,
                                     SocketChannel clientChannel) throws IOException {

        byte[] responseBytes = processResult.getResponseBytes();
        // TODO 从源头转为byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(responseBytes);
        while (byteBuffer.hasRemaining()) {
            // 这里的write不一定能一次写完（非阻塞的）
            clientChannel.write(byteBuffer);
        }
    }


    public void handleConnectionClose(Connection connection, SocketChannel channel) {

        if (connection != null) {

            //如果不为空，则说明给连接在ConnectionMgr的队列内
            ConnectionMgr.removeConnection(connection);
            return;
        }

        subEventLoop.closeClientChannel(channel);

    }


}
