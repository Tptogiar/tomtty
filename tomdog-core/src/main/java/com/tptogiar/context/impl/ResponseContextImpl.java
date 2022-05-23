package com.tptogiar.context.impl;

import com.tptogiar.context.RequestContext;
import com.tptogiar.context.ResponseContext;
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



    public ResponseContextImpl(RequestContext reqContext) {
        this.outputStream = reqContext.getHttpHandler().getOutputStream();
        this.inputStream =  reqContext.getHttpHandler().getInputStream();
    }


}
