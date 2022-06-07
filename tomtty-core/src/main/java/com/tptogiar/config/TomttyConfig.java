package com.tptogiar.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author Tptogiar
 * @description
 * @date 2022/6/6 - 12:50
 */
public class TomttyConfig {

    // 服务器地址
    public static String serverHostname;
    // 监听的端口
    public static int serverPort;

    // io模式，支持bio或nio
    public static String ioModel;

    // 从反应器数量
    public static int nioSubReactorCount;

    // 线程池配置
    public static int threadPoolCorePoolSize;
    public static int threadPoolMaximumPoolSize;
    public static int threadPoolKeepAliveTime;
    public static int thteadPoolBlockingQueueSize;

    // HTTP读取缓存区大小
    public static int httpReadBufferSize;
    public static int servletOutPutStreamBufferSize;

    // http keep-alice长连接保留时长
    public static int httpKeepAliveTime;
    // http keep-alice长连接最大数量
    public static int httpKeepAliveMaxConnection;

    // web.xml路径
    public static String webConfigXmlFilePath;

    public static String[] staticResourceRootPath = new String[8];

    // 默认页面路径
    public static String notFoundPagePath;
    public static String internalServerErrorPagePath;


    public static Properties config = new Properties();

    /**
     * 加载服务器配置
     * 用反射实现自动配置
     * @throws IOException
     */
    static {
        try {
            config.load(TomttyConfig.class.getResourceAsStream("/tomtty.config.properties"));
            Set<Map.Entry<Object, Object>> entries = config.entrySet();
            Iterator<Map.Entry<Object, Object>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> next = iterator.next();
                String key = (String) next.getKey();
                String value = (String) next.getValue();
                int index = 0;
                if (key.endsWith("]")) {
                    index = Integer.parseInt(key.substring(key.indexOf("[") + 1, key.length() - 1));
                    key = key.substring(0, key.length() - 3);
                }
                setValue(key, value, index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setValue(String key, String value, int index) throws Exception {
        Field field = TomttyConfig.class.getField(key);
        Class<?> type = field.getType();
        if (type == int.class) {
            field.setInt(null, Integer.parseInt(value));
        } else if (type == String.class) {
            field.set(null, value);
        } else if (type == String[].class) {
            String[] strs = (String[]) field.get(null);
            Array.set(strs, index, value);
        }
    }

    public static String curConfigStr(){
        StringBuilder stringBuilder =  null;
        try {
            InputStream inputStream = TomttyConfig.class.getResourceAsStream("/tomtty.config.properties");
            stringBuilder = new StringBuilder();
            ByteBuffer buffer = ByteBuffer.allocate(256);
            int read = 0;
            while ((read = inputStream.read(buffer.array()))!=-1){
                stringBuilder.append(new String(buffer.array(),0,read));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


}
