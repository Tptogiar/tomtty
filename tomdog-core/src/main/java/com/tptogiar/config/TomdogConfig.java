package com.tptogiar.config;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 13:01:00
 */
public class TomdogConfig {

    // 服务器地址
    public static String SERVER_HOSTNAME = "0.0.0.0";
    // 监听的端口
    public static int SERVER_PORT =8848;

    // 从反应器数量
    public static final int SERVER_NIO_SUB_REACTOR_COUNT = 6;

    // 线程池配置
    public static  int THREAD_POOL_CORE_POOL_SIZE = 10;
    public static  int THREAD_POOL_MAXIMUM_POOL_SIZE = 1000;
    public static  int THREAD_POOL_KEEP_ALIVE_TIME = 1000;
    public static  int THTEAD_POOL_BLOCKING_QUEUE_SIZE = 100;

    // HTTP读取缓存区大小
    public static int HTTP_READ_BUFFER_SIZE = 8*1024;
    public static int SERVLET_OUT_PUT_STREAM_BUFFER_SIZE = 8*1024;

    // web.xml路径
    public static String WEB_CONFIG_XML_FILE_PATH = "/web.xml";

    // 默认页面路径
    public static String NOT_FOUND_PAGE_PATH = "/default/pages/html/404.html";
    public static String INTERNAL_SERVER_ERROR_PAGE_PATH ="/default/pages/html/500.html";






}
