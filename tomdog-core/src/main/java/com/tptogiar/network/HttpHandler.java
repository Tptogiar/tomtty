package com.tptogiar.network;

import com.tptogiar.component.dispatch.ServletDispatcher;
import com.tptogiar.context.RequestContext;
import com.tptogiar.network.bio.builder.HttpResponseBuilder;
import com.tptogiar.network.bio.parser.HttpRequsetParser;
import com.tptogiar.servlet.Servlet;
import com.tptogiar.servlet.wrapper.HttpServletRequest;
import com.tptogiar.servlet.wrapper.HttpServletRequestWrapper;
import com.tptogiar.servlet.wrapper.HttpServletResponse;
import com.tptogiar.servlet.wrapper.HttpServletResponseWrapper;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Tptogiar
 * @description
 * @date 2022/5/28 - 14:10
 */
public abstract class HttpHandler{




    public byte[] process(byte[] requestData) throws Exception {
        RequestContext reqContext = HttpRequsetParser.parseHttpRequest(this, requestData);
        HttpServletRequest req = new HttpServletRequestWrapper(reqContext);
        HttpServletResponse resp = new HttpServletResponseWrapper(reqContext);
        Servlet result = ServletDispatcher.doDispatcher(req);
        result.doService(req, resp);

        HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(req, resp);
        httpResponseBuilder.buildResponse();


        return httpResponseBuilder.transferToResponseBytes();


    }


    public OutputStream getOutputStream() {
        return null;
    }

    public InputStream getInputStream() {
        return null;
    }
}
