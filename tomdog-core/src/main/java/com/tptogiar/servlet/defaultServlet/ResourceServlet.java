package com.tptogiar.servlet.defaultServlet;

import com.tptogiar.holder.ResourceHolder;
import com.tptogiar.servlet.HttpRequestServlet;
import com.tptogiar.servlet.HttpResponseServlet;
import com.tptogiar.servlet.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 23:14:00
 */
public class ResourceServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(ResourceServlet.class);

    private ResourceHolder resourceHolder;

    public ResourceServlet(ResourceHolder resourceHolder) {
        this.resourceHolder = resourceHolder;
    }

    @Override
    public void service(HttpRequestServlet req, HttpResponseServlet resp) {
        logger.info("Test ResourceServlet ----From ResourceServlet...");



    }
}
