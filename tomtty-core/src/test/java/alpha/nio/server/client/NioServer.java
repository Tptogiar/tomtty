package alpha.nio.server.client;


import com.tptogiar.config.TomttyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月29日 10:14:00
 */
public class NioServer {


    private static Logger logger = LoggerFactory.getLogger(NioServer.class);


    public static void main(String[] args) {

        try {

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            Selector selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(
                    new InetSocketAddress(TomttyConfig.serverHostname, TomttyConfig.serverPort));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, ByteBuffer.allocate(1024));

            logger.info("服务器已启动... hostname = {} port = {}", TomttyConfig.serverHostname, TomttyConfig.serverPort);

            while (true) {
                if (selector.select(1000) == 0) {
                    logger.info("当前无就绪fd");
                    continue;
                }

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        acceptEvent(selector, serverSocketChannel);
                    }
                    if (selectionKey.isReadable()) {
                        readEvent(selectionKey, selector);
                    }
                    if (selectionKey.isWritable()) {
                        writeEvent(selectionKey);
                    }
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void acceptEvent(Selector selector, ServerSocketChannel serverSocketChannel) throws IOException {

        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(10240));
    }


    public static void readEvent(SelectionKey selectionKey, Selector selector) throws IOException {

        SocketChannel channel = null;
        try {
            channel = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
            channel.register(selector, SelectionKey.OP_WRITE);
            int count = 0;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((count = channel.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer.array(), 0, count);
                buffer.clear();
            }
            // tcp连接断了
            if (count == -1) {
                logger.info("有一个TCP断连了");
                selectionKey.cancel();
                if (channel != null) {
                    channel.close();
                }
                return;
            }
            String result = new String(byteArrayOutputStream.toByteArray());
            System.out.println(result);
            buffer.clear();
            String content = "<html>\n" +
                    "　　<head>\n" +
                    "　　<title>HTTP响应示例</title>\n" +
                    "　　</head>\n" +
                    "　　<body>\n" +
                    "　　　　<p style='color:red'>Hello HTTP!</p>\n" +
                    "　　　　<p >From: Tptogiar Server</p>\n" +
                    "　　</body>\n" +
                    "</html>";
            String htmlStr =
                    "HTTP/1.1 200 OK\n" +
                            "Server:Tptogiar\n" +
                            "Content-Type:text/html; charset=utf-8\n" +
                            "\n" + content;
            System.out.println(content.getBytes().length);
            buffer.put(htmlStr.getBytes());
            buffer.flip();
            selectionKey.attach(buffer);


        } catch (IOException e) {
            if (channel != null) {
                channel.close();
            }
        }


    }


    public static void writeEvent(SelectionKey selectionKey) throws IOException {

        logger.info("有写出事件...");
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
        if (buffer == null) {
            return;
        }
        logger.info("写出内容: \n{}", new String(buffer.array()).trim());
        int write = channel.write(buffer);
        logger.debug("channel.write 的字节数 = " + write);
        selectionKey.cancel();
        channel.shutdownInput();
        channel.close();
    }


}
