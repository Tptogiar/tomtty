package com.tptogiar.network.packer;

import com.tptogiar.constant.CharContant;
import com.tptogiar.constant.CharsetProperties;
import com.tptogiar.constant.http.HttpResponseHeader;
import com.tptogiar.context.ResponseContext;
import com.tptogiar.temp.Cookie;
import com.tptogiar.temp.Header;
import com.tptogiar.temp.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 12:46:00
 */
public class HttpResponseBuilder {


    private Logger logger = LoggerFactory.getLogger(HttpResponseBuilder.class);


    private HttpServletResponse resp;
    private ResponseContext respContext;



    private StringBuilder headerAppender = new StringBuilder();



    private static String PROTOCOL = "HTTP/1.1";





    public HttpResponseBuilder(HttpServletResponse resp) {
        logger.info("封装Http响应...");
        this.resp = resp;
        this.respContext = resp.getResponseContext();
    }


    public void buildResponse() throws IOException {

        appendFirstLine();

        appendContentType();

        appendHeaders();

        appendCookies();



        byte[] body = resp.getBody();
        headerAppender.append(HttpResponseHeader.CONTENT_LENGTH)
                .append(body.length).append(CharContant.CRLF).append(CharContant.CRLF);

        byte[] header = headerAppender.toString().getBytes(CharsetProperties.UTF_8_CHARSET);
        byte[] responseBytes = new byte[header.length + body.length];
        System.arraycopy(header,0,responseBytes,0,header.length);
        System.arraycopy(body,0,responseBytes,header.length,body.length);



        OutputStream outputStream = respContext.getOutputStream();
        outputStream.write(responseBytes);
        outputStream.close();
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



}
