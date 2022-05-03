package com.tptogiar.servlet;

import com.tptogiar.temp.*;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 13:02:00
 */
public interface Servlet extends DispatchResult {


    void init();

    void destory();

    @Override
    void doService(HttpServletRequest req, HttpServletResponse resp) throws IOException;



}
