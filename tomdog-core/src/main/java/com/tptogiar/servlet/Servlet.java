package com.tptogiar.servlet;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 13:02:00
 */
public interface Servlet {


    void init();

    void destory();

    void service(HttpRequestServlet req, HttpResponseServlet resp);



}
