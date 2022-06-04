package com.tptogiar.servlet.component;

import java.io.ByteArrayOutputStream;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 15:14:00
 */
public interface ServletOutputStream {


    void write(byte[] buffer, int position, int len);


    ByteArrayOutputStream getOutputBuffer();


}
