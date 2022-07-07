package com.tptogiar.servlet.component;

import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * 封装一个buffer，将servlet的数据先写入这个buffer后再写给浏览器
 * 参考自tomcat
 *
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 15:15:00
 */
@Data
public class ServletOutputStreamImpl extends OutputStream implements ServletOutputStream {


    private ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();


    @Override
    public void write(int b) throws IOException {

        outputBuffer.write(b);
    }


    @Override
    public void write(byte[] b) throws IOException {

        outputBuffer.write(b);
    }


    @Override
    public void write(byte[] b, int off, int len) {

        outputBuffer.write(b, off, len);
    }


}
