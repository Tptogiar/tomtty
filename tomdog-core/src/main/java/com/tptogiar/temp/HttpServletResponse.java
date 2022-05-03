package com.tptogiar.temp;

import com.tptogiar.constant.http.HttpStatus;
import com.tptogiar.context.ResponseContext;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 10:44:00
 */
public interface HttpServletResponse {





    void addHeader(Header header);

    void addCookid(Cookie cookie);



    OutputStream getOutPutStream() throws IOException;

    ResponseContext getResponseContext();


    HttpStatus getStatus();
    void setStatus(HttpStatus status);

    String getContentType();
    void setContentType(String contentType);

    List<Header> getHeaders();

    List<Cookie> getCookies();

    byte[] getBody();
    void setBody(byte[] bytes);






}
