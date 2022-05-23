package com.tptogiar.servlet;

import com.tptogiar.temp.*;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 13:02:00
 */
public interface Servlet  {


    void init();

    void destory();

    void service(HttpServletRequest req, HttpServletResponse resp) throws IOException;


}
