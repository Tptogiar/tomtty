package com.tptogiar.servlet;

import com.tptogiar.servlet.component.ServletOutputStream;
import com.tptogiar.servlet.wrapper.HttpServletRequest;
import com.tptogiar.servlet.wrapper.HttpServletResponse;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * 负责在调用service.service之时，做一些前置或后置的工作
 *
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 13:54:00
 */
public abstract class AbstractHttpServlet implements Servlet {


    @Override
    public void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        service(req, resp);
        // 判断是resourceServlet还是普通的servlet，
        // resourceServlet中不会把数据写入ServletOutputStream，也就是为null
        if (resp.hasOutPutStream()) {
            ServletOutputStream outPutStream = (ServletOutputStream) resp.getOutputStream();
            resp.setBody(outPutStream.getOutputBuffer().toByteArray());
        }
    }


}
