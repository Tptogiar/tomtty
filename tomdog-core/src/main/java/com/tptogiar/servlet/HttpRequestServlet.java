package com.tptogiar.servlet;

import com.tptogiar.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 13:09:00
 */
public class HttpRequestServlet {

    private Logger logger = LoggerFactory.getLogger(HttpRequestServlet.class);

    private RequestContext requestContext;

    public HttpRequestServlet(RequestContext requestContext) {
        logger.info("封装RequsetServlet...");
        this.requestContext = requestContext;
    }
}
