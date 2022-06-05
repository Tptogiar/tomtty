package com.tptogiar.servlet;

import com.tptogiar.constant.http.HttpMethod;
import com.tptogiar.servlet.AbstractHttpServlet;
import com.tptogiar.servlet.Servlet;
import com.tptogiar.servlet.wrapper.HttpServletRequest;
import com.tptogiar.servlet.wrapper.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

/**
 * 供用户继承并重写service方法的servlet
 *
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
    public void service(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpMethod method = req.getMethod();
        if (method.equals(HttpMethod.GET)) {
            doGet(req, resp);
            return;
        } else if (method.equals(HttpMethod.POST)) {
            doPost(req, resp);
            return;
        }
        notSupportMethod(req, resp);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        notSupportMethod(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        notSupportMethod(req, resp);
    }

    // 给方法暂未被支持
    private void notSupportMethod(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        OutputStream outPutStream = resp.getOutputStream();
        String defaultMessage = "This method is not support...";
        outPutStream.write(defaultMessage.getBytes());
    }

}
