package alpha.test.bio.server.client;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 23:29:00
 */
public class BioServer {

    private Logger logger = LoggerFactory.getLogger(BioServer.class);


    @Test
    public void testServer() throws IOException, InterruptedException {

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("127.0.0.1",8840));
        while (true){
            logger.info("等待来自客户端的连接...");

            Socket clientSocket = serverSocket.accept();
            Thread thread = new Thread(new Handler(clientSocket));
            thread.start();
        }



    }




}

class Handler implements Runnable{

    private Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            byte[] buf = new byte[8196];
            int read = 0;
            try {
                read = inputStream.read(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(new String(buf,0,read));


//            InputStream inputStream = BioServer.class.getResourceAsStream("/src/main/webapp/Test.html");

//            StringBuilder sb = new StringBuilder();
//            byte[] bytes = new byte[1024];
//            int readCount = 0;
//            while ((readCount = inputStream.read(bytes))!=-1){
//                String cur = new String(bytes, 0, readCount);
//                sb.append(cur);
//            }
//            String str = sb.toString();

            String content ="<html>\n" +
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
                            "Content-Length:"+content.getBytes().length+"\n"+
                            "\n"+content;
            String temp = content+htmlStr;
            System.out.println(temp.length());
            outputStream.write(htmlStr.getBytes());
            outputStream.flush();
//            outputStream.close();
            System.out.println("in:");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}