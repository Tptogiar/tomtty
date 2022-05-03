package com.tptogiar.context.impl;

import com.tptogiar.context.RequestContext;
import com.tptogiar.context.ResponseContext;
import com.tptogiar.holder.ResourceHolder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 23:23:00
 */

@Data
public class ResponseContextImpl implements ResponseContext {


    private ResourceHolder resourceHolder;


    private OutputStream outputStream;
    private InputStream inputStream;



    public ResponseContextImpl(RequestContext reqContext) {
        this.outputStream = reqContext.getHttpHandler().getOutputStream();
        this.inputStream =  reqContext.getHttpHandler().getInputStream();
    }


}
