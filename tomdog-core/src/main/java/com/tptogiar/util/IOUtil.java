package com.tptogiar.util;

import java.io.*;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 11:30:00
 */
public class IOUtil {



    public static byte[] getBytesFromFile(String fileName) throws IOException {
        InputStream in = IOUtil.class.getResourceAsStream(fileName);
        if (in == null) {
            throw new FileNotFoundException();
        }
        return getBytesFromStream(in);
    }

    public static byte[] getBytesFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = in.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        in.close();
        return outStream.toByteArray();
    }

    public static void closeAll(Closeable... closeables) {
        // 遍历数组
        if (closeables != null) {
            for (Closeable c : closeables) {
                // 只要传进来的不是空的都给它把流关闭
                if (c != null) {
                    try {
                        c.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }





}
