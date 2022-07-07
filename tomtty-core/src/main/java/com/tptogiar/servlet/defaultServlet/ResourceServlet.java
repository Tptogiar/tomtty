package com.tptogiar.servlet.defaultServlet;


import com.tptogiar.config.TomttyConfig;
import com.tptogiar.constant.http.HttpContentType;
import com.tptogiar.network.bio.handler.ProcessResult;
import com.tptogiar.servlet.AbstractHttpServlet;
import com.tptogiar.servlet.HttpServlet;
import com.tptogiar.servlet.wrapper.HttpServletRequest;
import com.tptogiar.servlet.wrapper.HttpServletResponse;
import com.tptogiar.util.IOUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


/**
 * 静态资源处理
 *
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 00:06:00
 */
@Data
public class ResourceServlet extends HttpServlet {


    private static Logger logger = LoggerFactory.getLogger(ResourceServlet.class);

    private String uri;


    public ResourceServlet(String uri) {

        this.uri = uri;
    }


    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        req.setUri(uri);
        handleResource(req, resp);
    }


    /**
     * 读取文件二进制内容并添加到response的body里面
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public static void handleResource
    (HttpServletRequest req, HttpServletResponse resp) throws Exception {


        logger.info("处理静态资源...");
        String filePath = req.getUri();
        setContentType(filePath, resp);
        resp.setFileTransfer(true);

        File srcFile = IOUtil.getFileFromStaticResoucePath
                (TomttyConfig.staticResourceRootPath, filePath);

        if (srcFile == null) {
            AbstractHttpServlet servlet = new NotFoundServlet();
            servlet.doService(req, resp);
            resp.setFileTransfer(false);
            ProcessResult processResult = new ProcessResult(false);
            resp.attach(processResult);
            return;
        }


        ProcessResult processResult = new ProcessResult(true, srcFile);
        // 这里将文件信息封装进ProcessResult里面，待com.tptogiar.network.HttpHandler.process运行时在做响应的处理
        resp.attach(processResult);
    }


    /**
     * 根据文件类型，给response对象设置对应的content-type
     *
     * @param uriStr
     * @param resp
     */
    public static void setContentType(String uriStr, HttpServletResponse resp) {
        // TODO
        String uri = uriStr.toLowerCase();
        if (uri.endsWith(".html")) {
            resp.setContentType(HttpContentType.DEFAULT);
        }
        if (uri.endsWith(".css")) {
            resp.setContentType(HttpContentType.CSS);
        }
        if (uri.endsWith(".png") ||
                uri.endsWith(".ico") ||
                uri.endsWith(".jpg")
        ) {
            resp.setContentType(HttpContentType.IMAGE);
        }
    }


}
