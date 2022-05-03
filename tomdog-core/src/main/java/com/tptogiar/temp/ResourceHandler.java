package com.tptogiar.temp;

import com.tptogiar.constant.http.HttpContentType;
import com.tptogiar.network.packer.HttpResponseBuilder;
import com.tptogiar.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月03日 13:18:00
 */
public class ResourceHandler {

    private static Logger logger = LoggerFactory.getLogger(ResourceHandler.class);


    public static void handleResource(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("处理静态资源...");
        HttpResponseBuilder packer = new HttpResponseBuilder(resp);
        String uri = req.getUri();
        byte[] bytesFromFile = IOUtil.getBytesFromFile(uri);

        setContentType(uri,resp);
        resp.setBody(bytesFromFile);

        packer.buildResponse();

    }




    public static void setContentType(String uriStr,HttpServletResponse resp){
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
