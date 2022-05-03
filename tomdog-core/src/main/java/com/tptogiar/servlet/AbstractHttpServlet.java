package com.tptogiar.servlet;

import com.tptogiar.network.builder.HttpResponseBuilder;
import com.tptogiar.temp.DispatchResult;
import com.tptogiar.temp.HttpServletRequest;
import com.tptogiar.temp.HttpServletResponse;
import com.tptogiar.temp.ServletOutputStream;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 13:54:00
 */
public abstract class AbstractHttpServlet implements DispatchResult ,Servlet{



    @Override
    public void doService(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        service(req,resp);
        ServletOutputStream outPutStream = (ServletOutputStream) resp.getOutPutStream();
        resp.setBody(outPutStream.getOutputBuffer().toByteArray());

        HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(req, resp);
        httpResponseBuilder.buildResponse();
    }


    public abstract void service(HttpServletRequest req, HttpServletResponse resp) throws IOException;

}
