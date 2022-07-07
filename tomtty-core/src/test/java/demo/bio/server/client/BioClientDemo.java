package demo.bio.server.client;

import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 17:55:00
 */


public class BioClientDemo {


    @Test
    public void testBioClientDemo() {

        startBioClient();
    }


    public void startBioClient() {

        try {
            Socket s = new Socket("127.0.0.1", 8840);
            System.out.println("已连接上服务器...");
            //构建IO
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            //向服务器端发送一条消息
            bw.write("This is the message from 客户端...\n");
            bw.flush();

            //读取服务器返回的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String mess = br.readLine();
            System.out.println("收到服务器发送的消息：" + mess);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}