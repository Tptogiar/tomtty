package com.tptogiar.temp;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 10:14:00
 */
public interface DispatchResult {



    void service(HttpServletRequest req, HttpServletResponse resp) throws IOException;


}
