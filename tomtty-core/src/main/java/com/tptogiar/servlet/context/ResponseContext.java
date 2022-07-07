package com.tptogiar.servlet.context;

import java.io.IOException;
import java.io.OutputStream;


/**
 * 响应上下文信息
 *
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 00:33:00
 */
public interface ResponseContext {


    OutputStream getOutputStream() throws IOException;


    OutputStream getServletOutputStream();


    void createServletOutPutStream();


    boolean isFileTransfer();


    void setFileTransfer(boolean fileTransfer);


    void attach(Object attachment);


    Object getAttachment();


}
