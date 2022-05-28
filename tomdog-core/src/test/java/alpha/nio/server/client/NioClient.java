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
    public  void testWrite() throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",8848);
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
    public void testRead() throws IOException, InterruptedException {

        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8848));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ,SelectionKey.OP_CONNECT);
        socketChannel.write(ByteBuffer.wrap("sf水电费".getBytes()));


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
                    ByteBuffer buffer = ByteBuffer.allocate(8);
                    int read = channel.read(buffer);
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
