package demo.bio.server.client;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 17:54:00
 */

import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServerDemo {


    @Test
    public void testBioServerDemo() {
        startBioServer();
    }


    public void startBioServer() {
        try {
            ServerSocket ss = new ServerSocket(8840);
            System.out.println("启动服务器....");
            System.out.println("等待客户端连接...");
            Socket s = ss.accept();
            System.out.println("客户端:" + s.getInetAddress().getLocalHost() + "已连接到服务器");

            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            //读取客户端发送来的消息
            String mess = br.readLine();
            System.out.println("收到客户端发送的消息：" + mess);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bw.write("This is the message from 服务端...");
            bw.flush();
            bw.close();

            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
