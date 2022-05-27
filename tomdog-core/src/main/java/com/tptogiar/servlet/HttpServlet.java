package com.tptogiar.servlet;

import com.tptogiar.servlet.AbstractHttpServlet;
import com.tptogiar.servlet.Servlet;
import com.tptogiar.servlet.wrapper.HttpServletRequest;
import com.tptogiar.servlet.wrapper.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * 供用户继承并重写service方法的servlet
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 12:09:00
 */
public class HttpServlet extends AbstractHttpServlet implements Servlet {

    @Override
    public void init() {

    }

    @Override
    public void destory() {

    }


    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        OutputStream outPutStream = resp.getOutPutStream();
        String defaultMessage = "This get method is not support...";
        outPutStream.write(defaultMessage.getBytes());
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        OutputStream outPutStream = resp.getOutPutStream();
        String defaultMessage = "This post method is not support...";
        outPutStream.write(defaultMessage.getBytes());
    }


}
