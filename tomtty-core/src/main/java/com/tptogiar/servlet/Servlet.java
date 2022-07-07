package com.tptogiar.servlet;

import com.tptogiar.servlet.wrapper.HttpServletRequest;
import com.tptogiar.servlet.wrapper.HttpServletResponse;

import java.io.IOException;
import java.net.URISyntaxException;


/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 13:02:00
 */
public interface Servlet {


    default void init() {

    }


    default void destory() {

    }


    default void service(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException, URISyntaxException, Exception {

    }


    default void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }


    default void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }


    void doService(HttpServletRequest req, HttpServletResponse resp) throws IOException, Exception;


}
