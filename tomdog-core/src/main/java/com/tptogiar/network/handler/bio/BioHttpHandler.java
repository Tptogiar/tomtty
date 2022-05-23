package com.tptogiar.network.handler.bio;



import com.tptogiar.config.TomdogConfig;
import com.tptogiar.context.RequestContext;
import com.tptogiar.exception.RequestInvaildException;
import com.tptogiar.exception.ServletException;
import com.tptogiar.network.HttpHandler;
import com.tptogiar.servlet.Servlet;
import com.tptogiar.temp.*;
import com.tptogiar.component.dispatch.ServletDispatcher;
import com.tptogiar.network.parser.HttpRequsetParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.Socket;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 23:17:00
 */
public class BioHttpHandler implements HttpHandler {

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



            HttpRequsetParser requsetWrapper = new HttpRequsetParser(this,readBuffer);
            RequestContext reqContext = requsetWrapper.getReqContext();
            HttpServletRequest httpServletRequest = new HttpServletRequestWrapper(reqContext);
            HttpServletResponse httpServletResponse = new HttpServletResponseWrapper(reqContext);
            Servlet result = ServletDispatcher.doDispatcher(reqContext);
            result.service(httpServletRequest, httpServletResponse);
        } catch (ServletException e) {

            logger.info(e.getMessage());
        }catch (IOException  e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {

        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        logger.info("请求处理完成...");
    }




    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }


    @Override
    public InputStream getInputStream() {
        return inputStream;
    }
}
