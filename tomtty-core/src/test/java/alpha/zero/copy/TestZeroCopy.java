package alpha.zero.copy;

import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


/**
 * @author Tptogiar
 * @description
 * @date 2022/6/4 - 16:49
 */
public class TestZeroCopy {


    @Test
    public void testZeroCopy() throws IOException {

        System.out.println("testZeroCopy...");
        FileChannel fileChannel1 = new FileInputStream("test").getChannel();
        FileChannel fileChannel2 = new FileInputStream("test").getChannel();
        fileChannel1.transferTo(0, 1, fileChannel2);

        ServerSocket serverSocket = new ServerSocket();
        ServerSocketChannel serverSocketChannel = serverSocket.getChannel();

        Socket socket = new Socket("test", 8848);
        SocketChannel channel = socket.getChannel();


    }


}
