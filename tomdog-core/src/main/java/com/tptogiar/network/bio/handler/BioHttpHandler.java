package com.tptogiar.network.bio.handler;


import com.tptogiar.config.TomdogConfig;
import com.tptogiar.context.RequestContext;
import com.tptogiar.exception.RequestInvaildException;
import com.tptogiar.exception.ServletException;
import com.tptogiar.network.HttpHandler;
import com.tptogiar.network.bio.builder.HttpResponseBuilder;
import com.tptogiar.servlet.Servlet;
import com.tptogiar.servlet.wrapper.HttpServletRequest;
import com.tptogiar.servlet.wrapper.HttpServletRequestWrapper;
import com.tptogiar.servlet.wrapper.HttpServletResponse;
import com.tptogiar.servlet.wrapper.HttpServletResponseWrapper;
import com.tptogiar.component.dispatch.ServletDispatcher;
import com.tptogiar.network.bio.parser.HttpRequsetParser;
import com.tptogiar.util.IOUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * 用于处理一条http连接
 *
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 23:17:00
 */
public class BioHttpHandler extends HttpHandler implements Runnable {

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


    @SneakyThrows
    @Override
    public void run() {


        logger.info("开始处理请求...");
        int read = inputStream.read(readBuffer);
        if (read <= 0) {
            throw new RequestInvaildException();
        }

        byte[] responseBytes = process(readBuffer);

        writeResponseBytes(responseBytes);
        closeResource();


        logger.info("请求处理完成...");
    }


    public OutputStream getOutputStream() {
        return outputStream;
    }


    public InputStream getInputStream() {
        return inputStream;
    }


    public void writeResponseBytes(byte[] responseBytes) throws IOException {
        outputStream.write(responseBytes);
        outputStream.flush();
    }


    public void closeResource() throws IOException {
        IOUtil.closeAll(inputStream, outputStream);
    }

}
