package com.tptogiar.servlet.wrapper;

import com.tptogiar.context.RequestContext;

/**
 * 将request请求传递给用户servlet时，用该类来定义传递的request包含的内容
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 01:05:00
 */
public interface HttpServletRequest {

    String getUri();
    void setUri(String uri);

    RequestContext getRequestContext ();



}
