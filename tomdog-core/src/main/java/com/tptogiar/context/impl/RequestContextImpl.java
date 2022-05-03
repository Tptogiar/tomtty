package com.tptogiar.context.impl;




import com.tptogiar.constant.http.HttpMethod;
import com.tptogiar.context.RequestContext;

import com.tptogiar.network.HttpHandler;
import com.tptogiar.info.cookie.Cookie;
import lombok.Data;


import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 23:23:00
 */
@Data
public class RequestContextImpl implements RequestContext {

    private Socket socket;
    private Map<String, List<String>> headers;
    private HttpMethod method;
    private Cookie[] cookies;
    private Map<String,String> params;
    private String uri;


    private HttpHandler httpHandler;



}
