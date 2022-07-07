package com.tptogiar.network;

import com.tptogiar.component.dispatch.ServletDispatcher;
import com.tptogiar.servlet.context.RequestContext;
import com.tptogiar.network.bio.builder.HttpResponseBuilder;
import com.tptogiar.network.bio.handler.ProcessResult;
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
public abstract class HttpHandler {


    public ProcessResult process(byte[] requestData) throws Exception {


        RequestContext reqContext = HttpRequsetParser.parseHttpRequest(this, requestData);
        HttpServletRequest req = new HttpServletRequestWrapper(reqContext);
        HttpServletResponse resp = new HttpServletResponseWrapper(reqContext);
        Servlet result = ServletDispatcher.doDispatcher(req, resp);

        result.doService(req, resp);

        ProcessResult processResult = (ProcessResult) resp.attachment();
        if (processResult == null) {
            processResult = new ProcessResult();
        }

        HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(req, resp);
        byte[] responseHeader = httpResponseBuilder.buildResponseHeader(resp.getBody().length, processResult);
        // 当响应的是静态资源文件，为其添加对应的响应头
        if (resp.isFileTransfer()) {
            processResult.setResponseHeaderBytes(responseHeader);
        }
        // 对于普通响应，组合响应头和响应体
        else {
            byte[] responseBytes = HttpResponseBuilder.combineRespHeaderAndBody(responseHeader, resp.getBody());
            processResult.setResponseBytes(responseBytes);
        }
        processResult.setKeepAlive(reqContext.isKeepAlive());
        return processResult;
    }


    public OutputStream getOutputStream() {

        return null;
    }


    public InputStream getInputStream() {

        return null;
    }


}
