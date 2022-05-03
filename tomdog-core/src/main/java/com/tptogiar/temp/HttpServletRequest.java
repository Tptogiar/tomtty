package com.tptogiar.temp;

import com.tptogiar.context.RequestContext;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 01:05:00
 */
public interface HttpServletRequest {

    String getUri();
    void setUri(String uri);

    RequestContext getRequestContext ();



}
