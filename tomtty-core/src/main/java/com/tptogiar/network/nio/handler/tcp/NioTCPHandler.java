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
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 23:09:00
 */
@Data
public class NioTCPHandler {


    private static final Logger logger = LoggerFactory.getLogger(NioTCPHandler.class);

    private NioEventLoop subEventLoop;

    private Selector subSelector;

    private ConnectionMgr connectionMgr;


    public NioTCPHandler(NioEventLoop subEventLoop) {
        this.subEventLoop = subEventLoop;
        this.subSelector = subEventLoop.getSelector();
        this.connectionMgr = subEventLoop.getConnectionMgr();
    }

    public void read(SelectionKey selectionKey) throws IOException {
        logger.info("来自{}的读事件,并添加到连接管理器...", ((SocketChannel) selectionKey.channel()).getRemoteAddress());
        Connection connection = connectionMgr.addConnection(selectionKey);

        logger.info("开始读取通道内数据...");
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        // TODO 1024?
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int readCount = channel.read(buffer);
        buffer.flip();
        if (readCount == -1 || readCount == 0) {
            // TODO 没有数据直接关闭？ 会导致浏览器不能重用这个连接
            logger.info("通道内没有数据,关闭连接...");
            // 将关闭通道的动作延迟到下一次poll时在执行，
            // 防止引发SubPoller中scanSelectionKey中Set<SelectionKey>的ConcurrentModificationException
            if (connection!=null){
                // 如果该连接已经添加进connectionMgr，则让connectionMgr来处理其
                connectionMgr.removeConnection(connection);
                return;
            }
            subEventLoop.closeClientChannel(channel);
            return;
        }
        logger.info("读取通道内数据读取完毕，大小为{}...", buffer.limit());
        logger.debug("\n" + new String(buffer.array(), 0, buffer.limit()) + "\n");
        // 将读取的数据交给业务线程处理
        NioHttpHandler nioHttpHandler = new NioHttpHandler(buffer, selectionKey, subEventLoop);
        WorkerThreadPool.execute(nioHttpHandler);

    }


    public void write(SelectionKey selectionKey) throws IOException {
        logger.info("处理写事件...");
        ProcessResult processResult = (ProcessResult) selectionKey.attachment();
        SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
        selectionKey.isValid();
        if (processResult.isFileTransfer()){
            writeResponseFromFile(processResult,clientChannel);
        }else {
            writeResponse(processResult,clientChannel);
        }
        clientChannel.shutdownOutput();

        // 请求头中未包含Connection: keep-alive时
        if (connectionMgr.contains(selectionKey)
                && processResult.isKeepAlive()
                && selectionKey.isValid()){

            selectionKey.interestOps(SelectionKey.OP_READ);
            return;
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
        long l = srcFileChannel.transferTo(0, srcFileChannel.size(), clientChannel);
    }

    public static void writeResponse(ProcessResult processResult,
                                     SocketChannel clientChannel) throws IOException {

        byte[] responseBytes = processResult.getResponseBytes();
        // TODO 从源头转为byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(responseBytes);
        while(byteBuffer.hasRemaining()){
            // 这里的write不一定能一次写完（非阻塞的）
            clientChannel.write(byteBuffer);
        }
    }


}
