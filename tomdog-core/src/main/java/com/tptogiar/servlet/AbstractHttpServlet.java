package com.tptogiar.servlet;

import com.tptogiar.network.builder.HttpResponseBuilder;
import com.tptogiar.temp.HttpServletRequest;
import com.tptogiar.temp.HttpServletResponse;
import com.tptogiar.temp.ServletOutputStream;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 13:54:00
 */
public abstract class AbstractHttpServlet implements Servlet{



    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        service(req,resp);
        ServletOutputStream outPutStream = (ServletOutputStream) resp.getOutPutStream();
        resp.setBody(outPutStream.getOutputBuffer().toByteArray());

        HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(req, resp);
        httpResponseBuilder.buildResponse();
    }




}
