package com.tptogiar.config;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 13:01:00
 */
public class TomdogConfig {


    public static String SERVER_HOSTNAME = "127.0.0.1";
    public static int SERVER_PORT =8848;


    public static int HTTP_READ_BUFFER_SIZE = 8*1024;

    public static int SERVLET_OUT_PUT_STREAM_BUFFER_SIZE = 8*1024;

    public static String WEB_CONFIG_XML_FILE_PATH = "/web.xml";

    public static String NOT_FOUND_PAGE_PATH = "/default/pages/html/404.html";
    public static String INTERNAL_SERVER_ERROR_PAGE_PATH ="/default/pages/html/500.html";






}
