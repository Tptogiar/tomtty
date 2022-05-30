package alpha.nio.server.client;

import org.junit.Test;

import javax.swing.plaf.basic.BasicButtonUI;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月29日 11:14:00
 */
public class NioClient {


    @Test
    public  void testWrite2Server() throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",8488);
        socketChannel.configureBlocking(false);
        if (! socketChannel.connect(inetSocketAddress)){
            while (! socketChannel.finishConnect()){
                System.out.println("尝试重新连接");
                Thread.sleep(1000);
            }
        }
        String content = "sdf sdf 水电费";
        ByteBuffer wrap = ByteBuffer.wrap(content.getBytes());
        socketChannel.write(wrap);
        System.in.read();
    }


    @Test
    public void testWrite2SerAndReadFromSer() throws IOException, InterruptedException {

        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8848));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ,SelectionKey.OP_CONNECT);
        String writeContent = "GET / HTTP/1.1\r\n" +
                "Host: 127.0.0.1:8848\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8\r\n" +
                "Cookie: __utmz=96992031.1653279567.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utma=96992031.1616982073.1653279567.1653534814.1653718947.5; __utmc=96992031\r\n" +
                "\r\n" +
                "\r\n";
        socketChannel.write(ByteBuffer.wrap(writeContent.getBytes()));


        while (true){
            int count = selector.select();
            if (count<=0){
                continue;
            }

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                if (key.isConnectable()){
                    System.out.println("连接成功...");
                }
                if (key.isReadable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int read = channel.read(buffer);
                    buffer.flip();
                    if (read==-1){
                        channel.close();
                    }
                    System.out.println("read count = "+read);
                    System.out.println(new String(buffer.array()).trim());
                }
                iterator.remove();
            }

        }
    }





}
