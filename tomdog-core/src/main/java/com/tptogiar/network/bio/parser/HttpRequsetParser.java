package com.tptogiar.network.bio.parser;




import com.tptogiar.constant.CharContant;
import com.tptogiar.constant.CharsetProperties;
import com.tptogiar.constant.http.HttpRequestHeader;
import com.tptogiar.constant.http.HttpMethod;


import com.tptogiar.context.impl.RequestContextImpl;
import com.tptogiar.exception.RequestInvaildException;
import com.tptogiar.network.bio.handler.bio.BioHttpHandler;
import com.tptogiar.info.cookie.Cookie;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tptogiar.context.RequestContext;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 解析http请求报文
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 12:46:00
 */
@Data
public class HttpRequsetParser {


    private Logger logger = LoggerFactory.getLogger(HttpRequsetParser.class);

    private byte[] requestData;

    private RequestContext reqContext = new RequestContextImpl();


    public static RequestContext parseHttpRequest(
            BioHttpHandler httpHandler, byte[] requestData)
            throws RequestInvaildException {

        HttpRequsetParser httpRequsetParser = new HttpRequsetParser(httpHandler, requestData);
        return httpRequsetParser.getReqContext();

    }


    private HttpRequsetParser(BioHttpHandler httpHandler, byte[] requestData) throws RequestInvaildException {
        logger.info("解析HTTP二进制报文...");

        reqContext.setHttpHandler(httpHandler);
        this.requestData = requestData;

        String[] lines =null;
        try {
            String httpMessage = URLDecoder.decode(
                    new String(requestData, CharsetProperties.UTF_8_CHARSET),
                    CharsetProperties.UTF_8);
            lines =httpMessage.split(CharContant.CRLF);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (lines==null || lines.length<=1){
            throw new RequestInvaildException("请求头异常...");
        }
        parseHeaders(lines);
        if (reqContext.getHeaders()!=null
                && reqContext.getHeaders().containsKey(HttpRequestHeader.CONTENT_LENGTH)
                && ! "0".equals(reqContext.getHeaders().get(HttpRequestHeader.CONTENT_LENGTH).get(0))
        ){
            parseBody(lines[lines.length-1]);
        }

        logger.info("\n"+reqContext.toString());
    }




    public void parseHeaders(String[] lines){
        logger.info("解析请求报文...");
        String firstLine = lines[0];

        String[] firstLineSlices = firstLine.split(CharContant.BLANK);
        parseMethod(firstLineSlices[0]);

        String rawURL = firstLineSlices[1];
        String[] urlSlices = rawURL.split("\\?");

        parseUri(urlSlices[0]);

        if (urlSlices.length > 1) {
            parseParams(urlSlices[1]);
        }

        parseHeaderItems(lines);


        parseCookie(reqContext.getHeaders());

    }

    private void parseMethod(String method){
         reqContext.setMethod(HttpMethod.valueOf(method));
    }

    private void parseUri(String uri){
        reqContext.setUri(uri);
    }


    private void parseParams(String uriSlices){
        logger.debug("解析参数...");
        Map<String, String> params = reqContext.getParams();
        String[] keyAndValues= uriSlices.split("&");
        for (String kav : keyAndValues) {
            String[] kv = kav.split("=");
            params.put(kv[0],kv[1]);
        }
    }


    private void parseHeaderItems(String[] lines){
        String header;
        Map<String, List<String>> map = new HashMap<>();
        for (int i = 1; i < lines.length; i++) {
            header = lines[i];
            if (header.equals("")) {
                break;
            }
            int colonIndex = header.indexOf(':');
            String key = header.substring(0, colonIndex);
            String[] values = header.substring(colonIndex + 2).split(",");
            map.put(key, Arrays.asList(values));
        }
        reqContext.setHeaders(map);
    }

    private void parseCookie(Map<String, List<String>> headers){
        Cookie[] cookies =null;
        if (headers.containsKey("Cookie")) {
            String[] rawCookies = headers.get("Cookie").get(0).split("; ");
            cookies = new Cookie[rawCookies.length];
            for (int i = 0; i < rawCookies.length; i++) {
                String[] keyValue = rawCookies[i].split("=");
                cookies[i] = new Cookie(keyValue[0], keyValue[1]);
            }
            headers.remove("Cookie");
        } else {
            cookies = new Cookie[0];
        }
        reqContext.setCookies(cookies);
    }



    private void parseBody(String line) {
        logger.debug("解析请求体...");














    }



}
