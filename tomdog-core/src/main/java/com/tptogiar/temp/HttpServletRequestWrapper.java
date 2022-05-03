package com.tptogiar.temp;

import com.tptogiar.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 13:09:00
 */
public class HttpServletRequestWrapper implements HttpServletRequest {

    private Logger logger = LoggerFactory.getLogger(HttpServletRequestWrapper.class);

    private RequestContext requestContext;

    public HttpServletRequestWrapper(RequestContext requestContext) {
        logger.info("封装RequsetServlet...");
        this.requestContext = requestContext;
    }

    @Override
    public String getUri() {
        return requestContext.getUri();
    }

    @Override
    public void setUri(String uri) {
        requestContext.setUri(uri);
    }

    @Override
    public RequestContext getRequestContext() {
        return requestContext;
    }
}
