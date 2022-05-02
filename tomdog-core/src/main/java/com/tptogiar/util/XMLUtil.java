package com.tptogiar.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 00:27:00
 */
public class XMLUtil {

    public static Document getXMLDocument(InputStream inputStream){
        try {
            SAXReader reader = new SAXReader();
            return reader.read(inputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }







}
