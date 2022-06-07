package com.tptogiar.servlet.defaultServlet;

import com.tptogiar.config.TomttyConfig;
import com.tptogiar.servlet.HttpServlet;
import com.tptogiar.servlet.wrapper.HttpServletRequest;
import com.tptogiar.servlet.wrapper.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;

/**
 * 默认的404 处理
 *
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 10:41:00
 */
public class NotFoundServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(NotFoundServlet.class);


    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        logger.info("404 NOT FOUND  ----From NotFoundServlet...");
        logger.debug("request Uri = {} ,TomttyConfig.NOT_FOUND_PAGE_PATH = {}", req.getUri(), TomttyConfig.notFoundPagePath);
        if (TomttyConfig.notFoundPagePath.equals(req.getUri())){
            // 默认的404.html也找不到,则以文字形式回写
            OutputStream outPutStream = resp.getOutputStream();
            String jsonStr = "<h1>404 Not Found</h1>";
            outPutStream.write(jsonStr.getBytes());
            return;
        }

        req.setUri(TomttyConfig.notFoundPagePath);
        ResourceServlet notFoundHtmlResourceServlet = new ResourceServlet(req.getUri());
        notFoundHtmlResourceServlet.service(req, resp);
    }

}
