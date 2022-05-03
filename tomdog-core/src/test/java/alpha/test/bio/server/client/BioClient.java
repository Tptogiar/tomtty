package alpha.test.bio.server.client;


import org.junit.Test;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 23:29:00
 */
public class BioClient {


    @Test
    public void testClient() throws IOException {

        Socket socket = new Socket("127.0.0.1", 8840);
        OutputStream outputStream = socket.getOutputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        outputStream.write("Test Request Content...".getBytes());
        outputStream.flush();
        byte[] buf = null;
        try {
            buf = new byte[bufferedInputStream.available()];
            int len = bufferedInputStream.read(buf);
            if (len <= 0) {
                System.out.println("无数据...");

                outputStream.write("Test Request Content2...".getBytes());
                outputStream.flush();
                return;
            }
            System.out.println(new String(buf));



        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
