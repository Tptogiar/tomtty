package com.tptogiar.servlet.wrapper;

import com.tptogiar.constant.http.HttpMethod;
import com.tptogiar.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SocketChannel;

/**
 * RequestContext类的包装类
 * 只对用户暴露context内的部分内容
 * 将request传递给用户的servlet时，传递的是这个类的示例
 *
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

    @Override
    public HttpMethod getMethod() {
        return requestContext.getMethod();
    }

    @Override
    public SocketChannel getSocketChannel() {
        return requestContext.getSocketChannel();
    }


}
