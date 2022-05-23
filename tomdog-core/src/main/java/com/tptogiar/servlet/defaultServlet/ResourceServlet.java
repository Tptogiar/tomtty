package com.tptogiar.servlet.defaultServlet;


import com.tptogiar.constant.http.HttpContentType;
import com.tptogiar.network.builder.HttpResponseBuilder;
import com.tptogiar.servlet.Servlet;
import com.tptogiar.temp.HttpServletRequest;
import com.tptogiar.temp.HttpServletResponse;
import com.tptogiar.util.IOUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 00:06:00
 */
@Data
public class ResourceServlet implements Servlet {


    private static Logger logger = LoggerFactory.getLogger(ResourceServlet.class);

    private String uri;



    public ResourceServlet(String uri) {
        this.uri = uri;
    }

    @Override
    public void init() {

    }

    @Override
    public void destory() {

    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setUri(uri);
        handleResource(req,resp);
    }

    public static void handleResource(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("处理静态资源...");
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder(req,resp);
        String uri = req.getUri();
        byte[] bytesFromFile = IOUtil.getBytesFromFile(uri);

        setContentType(uri,resp);
        resp.setBody(bytesFromFile);

        responseBuilder.buildResponse();

    }

    public static void setContentType(String uriStr,HttpServletResponse resp){
        // TODO
        String uri = uriStr.toLowerCase();
        if (uri.endsWith(".html")){
            resp.setContentType(HttpContentType.DEFAULT);
        }
        if (uri.endsWith(".css")){
            resp.setContentType(HttpContentType.CSS);
        }
        if (uri.endsWith(".png") ||
                uri.endsWith(".ico") ||
                uri.endsWith(".jpg")
        ){
            resp.setContentType(HttpContentType.IMAGE);
        }
    }






}
