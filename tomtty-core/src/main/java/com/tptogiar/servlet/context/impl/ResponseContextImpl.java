package com.tptogiar.servlet.context.impl;

import com.tptogiar.servlet.context.RequestContext;
import com.tptogiar.servlet.context.ResponseContext;
import com.tptogiar.servlet.component.ServletOutputStream;
import com.tptogiar.servlet.component.ServletOutputStreamImpl;
import lombok.Data;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 23:23:00
 */

@Data
public class ResponseContextImpl implements ResponseContext {


    private OutputStream outputStream;

    private InputStream inputStream;

    private boolean isFileTransfer;

    private ServletOutputStream servletOutputStream;

    private Object attachment;


    public ResponseContextImpl(RequestContext reqContext) {

        this.outputStream = reqContext.getHttpHandler().getOutputStream();
        this.inputStream = reqContext.getHttpHandler().getInputStream();
    }


    @Override
    public OutputStream getServletOutputStream() {

        return (OutputStream) servletOutputStream;
    }


    @Override
    public void createServletOutPutStream() {

        servletOutputStream = new ServletOutputStreamImpl();
    }


    @Override
    public void attach(Object attachment) {

        this.attachment = attachment;
    }


}
