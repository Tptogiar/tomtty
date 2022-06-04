package com.tptogiar.component.dispatch;

import com.tptogiar.holder.ServletHolder;
import com.tptogiar.servlet.Servlet;
import com.tptogiar.servlet.defaultServlet.NotFoundServlet;
import com.tptogiar.servlet.defaultServlet.ResourceServlet;
import com.tptogiar.servlet.wrapper.HttpServletRequest;
import com.tptogiar.servlet.wrapper.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 进行请求分发，分发到对应的servlet
 *
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 12:59:00
 */
public class ServletDispatcher {

    private static Logger logger = LoggerFactory.getLogger(ServletDispatcher.class);

    private static Map<String, ServletHolder> nameServletMap = new HashMap<>();
    private static Map<String, String> patternNameMap = new HashMap<>();


    private static Map<String, ResourceServlet> uriResourceMap = new HashMap<>();


    static {

        try {

            ServletCollector.collectServlet(nameServletMap, patternNameMap);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    /**
     * 分发处理
     *
     * @param request
     * @param resp
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Servlet doDispatcher(HttpServletRequest request, HttpServletResponse resp) throws IllegalAccessException, InstantiationException {
        String uri = request.getUri();
        logger.info("request uri = {}", uri);
        Servlet servlet = matchingServlet(uri);

        if (servlet == null) {
            servlet = matchingResource(uri);
        }
        if (servlet instanceof ResourceServlet){
            resp.setFileTransfer(true);
        }

        if (servlet == null) {
            servlet = (Servlet) new NotFoundServlet();
        }

        return servlet;
    }


    /**
     * 根据uri匹配servlet
     *
     * @param uri
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Servlet matchingServlet(String uri) throws IllegalAccessException, InstantiationException {
        String servletName = patternNameMap.get(uri);
        ServletHolder servletHolder = nameServletMap.get(servletName);
        if (servletHolder == null) {
            return null;
        }
        return (Servlet) servletHolder.getSevletClass().newInstance();
    }


    public static Servlet matchingResource(String uri) {
        URL resource = ServletDispatcher.class.getResource(uri);
        // TODO 首页处理
        if ("/".equals(uri)) {
            return (Servlet) new ResourceServlet("/default/pages/html/index.html");
        }
        if (resource == null) {
            return null;
        }
        return (Servlet) new ResourceServlet(uri);
    }


}
