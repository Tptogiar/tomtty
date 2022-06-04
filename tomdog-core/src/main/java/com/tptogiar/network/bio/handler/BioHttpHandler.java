package com.tptogiar.network.bio.handler;


import com.tptogiar.config.TomdogConfig;
import com.tptogiar.network.HttpHandler;
import com.tptogiar.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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


    @Override
    public void run() {


        try {
            logger.info("开始处理请求...");
            int read = inputStream.read(readBuffer);
            if (read <= 0) {
                closeResource();
                return;
            }

            ProcessResult processResult = process(readBuffer);

            writeResponseBytes(processResult.getResponseBytes());
            closeResource();
        } catch (Exception e) {
            e.printStackTrace();
        }


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
