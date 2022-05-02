package com.tptogiar.network.handler.bio;


import com.tptogiar.config.TomdogConfig;
import com.tptogiar.context.RequestContext;
import com.tptogiar.exception.RequestInvaildException;
import com.tptogiar.exception.ServletException;
import com.tptogiar.servlet.HttpRequestServlet;
import com.tptogiar.servlet.HttpResponseServlet;
import com.tptogiar.component.ServletDispatcher;
import com.tptogiar.network.handler.HttpHandler;
import com.tptogiar.network.wrapper.HttpRequsetWrapper;
import com.tptogiar.servlet.Servlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 23:17:00
 */
public class BioHttpHandler extends HttpHandler {

    private Logger logger = LoggerFactory.getLogger(BioHttpHandler.class);

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private byte[] readBuffer = new byte[TomdogConfig.HTTP_READ_BUFFER_SIZE];


    public BioHttpHandler(Socket socket) throws IOException {
        this.socket = socket;
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
    }


    @Override
    public void run() {
        try {

            logger.info("开始处理请求...");
            int read = inputStream.read(readBuffer);
            if (read<=0){
                throw new RequestInvaildException();
            }

            HttpRequsetWrapper requsetWrapper = new HttpRequsetWrapper(readBuffer);
            RequestContext reqContext = requsetWrapper.getReqContext();
            HttpRequestServlet httpRequestServlet = new HttpRequestServlet(reqContext);
            HttpResponseServlet httpResponseServlet = new HttpResponseServlet();
            Servlet resutl = ServletDispatcher.doDispatcher(reqContext);
            resutl.service(httpRequestServlet, httpResponseServlet);



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


            outputStream.write(htmlStr.getBytes());
            outputStream.flush();
            outputStream.close();

            logger.info("请求处理完成...");
        } catch (ServletException e) {

            logger.info(e.getMessage());
            e.printStackTrace();
            String content = "<html>\n" +
                    "　　<head>\n" +
                    "　　<title>HTTP响应示例</title>\n" +
                    "　　</head>\n" +
                    "　　<body>\n" +
                    "　　　　<p style='color:red'>Error</p>\n" +
                    "　　　　<p >From: Tptogiar Server</p>\n" +
                    "　　</body>\n" +
                    "</html>";
            String htmlStr =
                    "HTTP/1.1 200 OK\n" +
                            "Server:Tptogiar\n" +
                            "Content-Type:text/html; charset=utf-8\n" +
                            "\n" + content;


            try {
                outputStream.write(htmlStr.getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }



        }catch (IOException  e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {

        } catch (InstantiationException e) {
            e.printStackTrace();
        }


    }




}
