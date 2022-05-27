package com.tptogiar.component.dispatch;

import com.tptogiar.config.TomdogConfig;
import com.tptogiar.holder.ServletHolder;
import com.tptogiar.util.ReflectUtil;
import com.tptogiar.util.XMLUtil;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.InputStream;
import java.util.*;

/**
 * 收集servlet注册信息
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 18:03:00
 */
public class ServletCollector {



    public static void collectServlet(
            Map<String, ServletHolder> nameServletMap,
             Map<String, String> patternNameMap)
            throws ClassNotFoundException {


        InputStream inputStream = ServletCollector.class
                .getResourceAsStream(TomdogConfig.WEB_CONFIG_XML_FILE_PATH);


        Document doc = XMLUtil.getXMLDocument(inputStream);
        if (doc==null){
            return ;
        }
        Element docRoot = doc.getRootElement();

        parseServlet(nameServletMap,patternNameMap,docRoot);

    }



    public static void parseServlet
            (Map<String, ServletHolder> nameServletMap,
             Map<String, String> patternNameMap, Element root)
            throws ClassNotFoundException {


        List<Element> servlets = root.elements("servlet");
        for (Element servletEle : servlets) {
            String name = servletEle.element("servlet-name").getText();
            String calssName = servletEle.element("servlet-class").getText();
            ServletHolder holder = packingServletHolder(calssName);
            nameServletMap.put(name,holder);
        }

        List<Element> servletMapping = root.elements("servlet-mapping");
        for (Element mapping : servletMapping) {
            List<Element> urlPatterns = mapping.elements("url-pattern");
            String value = mapping.element("servlet-name").getText();
            for (Element urlPattern : urlPatterns) {
                patternNameMap.put(urlPattern.getText(), value);
            }
        }

    }




    public static ServletHolder packingServletHolder(String className) throws ClassNotFoundException {
        Class clazz = ReflectUtil.getClassForName(className);
        return new ServletHolder(clazz, className);
    }







}
