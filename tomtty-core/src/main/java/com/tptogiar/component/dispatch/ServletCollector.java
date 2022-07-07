package com.tptogiar.component.dispatch;

import com.tptogiar.config.TomttyConfig;
import com.tptogiar.holder.ServletHolder;
import com.tptogiar.util.ReflectUtil;
import com.tptogiar.util.XMLUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Map;


/**
 * 收集servlet注册信息
 *
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 18:03:00
 */
public class ServletCollector {


    private static Logger logger = LoggerFactory.getLogger(ServletCollector.class);


    public static void collectServlet(
            Map<String, ServletHolder> nameServletMap,
            Map<String, String> patternNameMap)
            throws ClassNotFoundException {


        InputStream inputStream = ServletCollector.class
                .getResourceAsStream(TomttyConfig.webConfigXmlFilePath);


        Document doc = XMLUtil.getXMLDocument(inputStream);
        if (doc == null) {
            logger.error("web.xml 文件获取失败...");
            return;
        }
        Element docRoot = doc.getRootElement();

        parseServlet(nameServletMap, patternNameMap, docRoot);

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
            nameServletMap.put(name, holder);
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
