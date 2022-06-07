package com.tptogiar.network.bio.parser;


import com.tptogiar.constant.CharContant;
import com.tptogiar.constant.CharsetProperties;
import com.tptogiar.constant.http.HttpMethod;
import com.tptogiar.constant.http.HttpRequestHeader;
import com.tptogiar.exception.RequestInvaildException;
import com.tptogiar.info.cookie.Cookie;
import com.tptogiar.network.HttpHandler;
import com.tptogiar.servlet.context.RequestContext;
import com.tptogiar.servlet.context.impl.RequestContextImpl;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析http请求报文
 *
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
            HttpHandler httpHandler, byte[] requestData) throws Exception {

        HttpRequsetParser httpRequsetParser = new HttpRequsetParser(httpHandler, requestData);
        return httpRequsetParser.getReqContext();

    }

    /**
     * 解析http请求
     *
     * @param httpHandler
     * @param requestData
     * @throws RequestInvaildException
     */
    private HttpRequsetParser(HttpHandler httpHandler, byte[] requestData) throws Exception {
        logger.info("解析HTTP二进制报文...");

        reqContext.setHttpHandler(httpHandler);
        this.requestData = requestData;

        String[] lines = null;

        String httpMessage = URLDecoder.decode(
                new String(requestData, CharsetProperties.UTF_8_CHARSET),
                CharsetProperties.UTF_8);
        lines = httpMessage.split(CharContant.CRLF);


        if (lines == null || lines.length <= 1) {
            throw new RequestInvaildException("请求头异常...");
        }

        // 解析请求头
        parseHeaders(lines);


        if (hasRequestBody(reqContext)) {
            parseBody(lines[lines.length - 1]);
        }


        logger.info("\n" + reqContext.toString());
    }


    /**
     * 解析请求头
     *
     * @param lines
     */
    public void parseHeaders(String[] lines) {
        logger.info("解析请求报文...");

        parseRequestLine(lines);

        parseHeaderItems(lines);

        parseCookie(reqContext.getHeaders());

        parseConnection(reqContext.getHeaders());

    }



    /**
     * 解析请求行
     * @param lines
     */
    private void parseRequestLine(String[] lines){
        String firstLine = lines[0];

        String[] firstLineSlices = firstLine.split(CharContant.BLANK);
        parseMethod(firstLineSlices[0]);

        String rawURL = firstLineSlices[1];
        String[] urlSlices = rawURL.split("\\?");

        parseUri(urlSlices[0]);

        if (urlSlices.length > 1) {
            parseParams(urlSlices[1]);
        }
    }

    private void parseMethod(String method) {
        reqContext.setMethod(HttpMethod.valueOf(method));
    }



    private void parseUri(String uri) {
        reqContext.setUri(uri);
    }


    private void parseParams(String uriSlices) {
        logger.debug("解析参数...");
        Map<String, String> params = reqContext.getParams();
        String[] keyAndValues = uriSlices.split("&");
        for (String kav : keyAndValues) {
            String[] kv = kav.split("=");
            params.put(kv[0], kv[1]);
        }
    }

    /**
     * 解析请求头部
     * @param lines
     */
    private void parseHeaderItems(String[] lines) {
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

    private void parseCookie(Map<String, List<String>> headers) {
        Cookie[] cookies = null;
        if (headers.containsKey(HttpRequestHeader.COOKIE)) {
            String[] rawCookies = headers.get(HttpRequestHeader.COOKIE).get(0).split("; ");
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


    private void parseConnection(Map<String, List<String>> headers) {
        if (headers.containsKey(HttpRequestHeader.CONNECTION)){
            reqContext.setKeepAlive(true);
            return;
        }
        reqContext.setKeepAlive(false);
    }


    /**
     * 判断是否有请求体
     *
     * @param reqContext
     * @return
     */
    public static boolean hasRequestBody(RequestContext reqContext) {
        Map<String, List<String>> headers = reqContext.getHeaders();
        if (headers != null) {
            return false;
        }
        if (!headers.containsKey(HttpRequestHeader.CONTENT_LENGTH)) {
            return false;
        }
        if (!"0".equals(reqContext.getHeaders().get(HttpRequestHeader.CONTENT_LENGTH).get(0))) {
            return false;
        }
        return true;
    }


    // 解析请求体
    private void parseBody(String line) {
        logger.debug("解析请求体...");
        // TODO 解析请求体
    }


}
