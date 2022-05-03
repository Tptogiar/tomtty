package com.tptogiar.network.builder;

import com.tptogiar.constant.CharContant;
import com.tptogiar.constant.CharsetProperties;
import com.tptogiar.constant.http.HttpContentType;
import com.tptogiar.constant.http.HttpResponseHeader;
import com.tptogiar.context.ResponseContext;
import com.tptogiar.network.HttpHandler;
import com.tptogiar.temp.Cookie;
import com.tptogiar.temp.Header;
import com.tptogiar.temp.HttpServletRequest;
import com.tptogiar.temp.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 12:46:00
 */
public class HttpResponseBuilder {


    private Logger logger = LoggerFactory.getLogger(HttpResponseBuilder.class);


    private HttpServletRequest req;
    private HttpServletResponse resp;
    private ResponseContext respContext;


    private StringBuilder headerAppender = new StringBuilder();


    private static String PROTOCOL = "HTTP/1.1";





    public HttpResponseBuilder(HttpServletRequest req, HttpServletResponse resp) {
        logger.info("封装Http响应...");
        this.req = req;
        this.resp = resp;
        this.respContext = resp.getResponseContext();
    }


    public void buildResponse() throws IOException {

        appendFirstLine();

        appendContentType();

        appendHeaders();

        appendCookies();



        byte[] body = resp.getBody();
        headerAppender.append(HttpResponseHeader.CONTENT_LENGTH).append(body.length)
                .append(CharContant.CRLF).append(CharContant.CRLF);

        byte[] header = headerAppender.toString().getBytes(CharsetProperties.UTF_8_CHARSET);
        byte[] responseBytes = new byte[header.length + body.length];
        System.arraycopy(header,0,responseBytes,0,header.length);
        System.arraycopy(body,0,responseBytes,header.length,body.length);



        OutputStream outputStream = req.getRequestContext().getHttpHandler().getOutputStream();
        outputStream.write(responseBytes);
        outputStream.flush();
        closeResource();


        logger.debug(printHttpResponseMsg(resp,header,body));
    }

    private void appendFirstLine(){
        // HTTP/1.1 200 OK\r\n
        headerAppender.append(PROTOCOL).append(CharContant.BLANK)
                .append(resp.getStatus().getCode()).append(CharContant.BLANK)
                .append(resp.getStatus()).append(CharContant.CRLF);
    }


    private void appendContentType(){
        headerAppender.append(HttpResponseHeader.CONTENT_TYPE)
                .append(resp.getContentType()).append(CharContant.CRLF);
    }

    private void appendHeaders(){
        List<Header> headers = resp.getHeaders();
        if (headers!=null){
            for (Header header : headers) {
                headerAppender.append(header.getKey()).append(CharContant.COLON).append(CharContant.BLANK)
                        .append(header.getValue()).append(CharContant.CRLF);
            }
        }
    }


    private void appendCookies(){
        List<Cookie> cookies = resp.getCookies();
        if (cookies!=null){
            // TODO 添加Cookie
        }
    }


    private void appendBody(){




    }



    public void closeResource() throws IOException {

        HttpHandler httpHandler = req.getRequestContext().getHttpHandler();
        httpHandler.getInputStream().close();
        httpHandler.getOutputStream().close();
    }





    public String printHttpResponseMsg(HttpServletResponse resp,byte[] header,byte[]body){
        StringBuilder sb = new StringBuilder("\n================================\n" + new String(header));
        if (resp.getContentType()==HttpContentType.DEFAULT){
            sb.append(new String(body));
        }
        sb.append("\n================================\n");
        return sb.toString();
    }



}
