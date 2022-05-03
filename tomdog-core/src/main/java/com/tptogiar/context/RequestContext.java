package com.tptogiar.context;

import com.tptogiar.constant.http.HttpMethod;

import com.tptogiar.network.HttpHandler;
import com.tptogiar.info.cookie.Cookie;

import java.util.List;
import java.util.Map;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 09:44:00
 */
public interface RequestContext {

    void setHttpHandler(HttpHandler httpHandler);



    Map<String, List<String>> getHeaders() ;

    void setHeaders(Map<String, List<String>> headers) ;

    HttpMethod getMethod() ;

    void setMethod(HttpMethod method) ;

    Cookie[] getCookies() ;

    void setCookies(Cookie[] cookies) ;

    Map<String, String> getParams() ;

    void setParams(Map<String, String> params) ;

    String getUri() ;

    void setUri(String uri) ;

    HttpHandler getHttpHandler();













}
