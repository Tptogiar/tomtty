package com.tptogiar.servlet.defaultServlet;

import com.tptogiar.config.TomdogConfig;
import com.tptogiar.servlet.HttpServlet;
import com.tptogiar.servlet.wrapper.HttpServletRequest;
import com.tptogiar.servlet.wrapper.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 10:41:00
 */
public class NotFoundServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(NotFoundServlet.class);


    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("404 NOT FOUND  ----From NotFoundServlet...");
        logger.debug("request Uri = {} ,TomdogConfig.NOT_FOUND_PAGE_PATH = {}",req.getUri(),TomdogConfig.NOT_FOUND_PAGE_PATH);
        req.setUri(TomdogConfig.NOT_FOUND_PAGE_PATH);
        ResourceServlet notFoundHtmlResourceServlet = new ResourceServlet(req.getUri());
        notFoundHtmlResourceServlet.service(req,resp);
    }
}
