package com.tptogiar.holder;


import com.tptogiar.temp.DispatchResult;
import com.tptogiar.temp.HttpServletRequest;
import com.tptogiar.temp.HttpServletResponse;
import com.tptogiar.temp.ResourceHandler;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
    public void doService(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setUri(uri);
        ResourceHandler.handleResource(req,resp);
    }








}
