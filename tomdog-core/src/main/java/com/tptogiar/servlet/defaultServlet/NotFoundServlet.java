package com.tptogiar.servlet.defaultServlet;

import com.tptogiar.holder.ResourceHolder;
import com.tptogiar.temp.HttpServletRequest;
import com.tptogiar.temp.HttpServletRequestWrapper;
import com.tptogiar.temp.HttpServletResponse;
import com.tptogiar.temp.HttpServletResponseWrapper;
import com.tptogiar.servlet.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 10:41:00
 */
public class NotFoundServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(NotFoundServlet.class);


    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) {
        logger.info("404 NOT FOUND  ----From NotFoundServlet...");
//        ResourceHolder notFoundHolder = new ResourceHolder();


    }
}
