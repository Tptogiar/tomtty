package com.tptogiar.context;

import com.tptogiar.servlet.component.ServletOutputStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 00:33:00
 */
public interface ResponseContext {



    OutputStream getOutputStream() throws IOException;

    OutputStream getServletOutputStream();


    void createServletOutPutStream();
}
