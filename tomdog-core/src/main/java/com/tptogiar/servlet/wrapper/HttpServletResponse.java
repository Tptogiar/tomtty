package com.tptogiar.servlet.wrapper;

import com.tptogiar.constant.http.HttpStatus;
import com.tptogiar.context.ResponseContext;
import com.tptogiar.info.cookie.Cookie;
import com.tptogiar.info.header.Header;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 将response请求传递给用户servlet时，用该类来定义传递的response包含的内容
 *
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 10:44:00
 */
public interface HttpServletResponse {


    void addHeader(Header header);

    void addCookid(Cookie cookie);


    OutputStream getOutputStream() throws IOException;


    boolean hasOutPutStream() throws IOException;


    ResponseContext getResponseContext();


    HttpStatus getStatus();

    void setStatus(HttpStatus status);

    String getContentType();

    void setContentType(String contentType);

    List<Header> getHeaders();

    List<Cookie> getCookies();

    byte[] getBody();

    void setBody(byte[] bytes);



    boolean isFileTransfer();

    void setFileTransfer(boolean fileTransfer);

    void attach(Object attach);

    Object attachment();

}
