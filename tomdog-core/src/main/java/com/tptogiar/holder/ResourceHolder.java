package com.tptogiar.holder;


import com.tptogiar.network.packer.HttpResponseBuilder;
import com.tptogiar.temp.DispatchResult;
import com.tptogiar.temp.HttpServletRequest;
import com.tptogiar.temp.HttpServletResponse;
import com.tptogiar.temp.ResourceHandler;
import com.tptogiar.util.IOUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 00:06:00
 */
@Data
public class ResourceHolder implements DispatchResult {


    private Logger logger = LoggerFactory.getLogger(ResourceHolder.class);

    private String uri;



    public ResourceHolder(String uri) {
        this.uri = uri;
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ResourceHandler.handleResource(req,resp);

    }








}
