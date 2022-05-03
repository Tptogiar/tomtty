package com.tptogiar.component;

import com.tptogiar.context.RequestContext;
import com.tptogiar.holder.ResourceHolder;
import com.tptogiar.holder.ServletHolder;
import com.tptogiar.servlet.defaultServlet.NotFoundServlet;
import com.tptogiar.servlet.Servlet;
import com.tptogiar.temp.DispatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 12:59:00
 */
public class ServletDispatcher {

    private static Logger logger = LoggerFactory.getLogger(ServletDispatcher.class);

    private static Map<String, ServletHolder> nameServletMap = new HashMap<>();
    private static Map<String,String> patternNameMap = new HashMap<>();


    private static Map<String,ResourceHolder> uriResourceMap = new HashMap<>();


    static {

        try {
            ServletCollector.collectServlet(nameServletMap,patternNameMap);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }





    public static DispatchResult doDispatcher(RequestContext requestContext) throws IllegalAccessException, InstantiationException {
        String uri = requestContext.getUri();
        logger.info("request uri = {}",uri);
        DispatchResult servlet = matchingServlet(uri);

        if (servlet == null){
            servlet = matchingResource(uri);
        }

        if (servlet==null){
            servlet = new NotFoundServlet();
        }

        return servlet;
    }





    public static Servlet matchingServlet(String uri) throws IllegalAccessException, InstantiationException {
        String servletName = patternNameMap.get(uri);
        ServletHolder servletHolder = nameServletMap.get(servletName);
        if (servletHolder==null){
            return null;
        }
        return (Servlet) servletHolder.getSevletClass().newInstance();
    }


    public static DispatchResult matchingResource(String uri){
        URL resource = ServletDispatcher.class.getResource(uri);

        if (resource==null){
            return null;
        }
        return new ResourceHolder(uri);
    }








}
