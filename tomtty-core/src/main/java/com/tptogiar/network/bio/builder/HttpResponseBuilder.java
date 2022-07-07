package com.tptogiar.network.bio.builder;

import com.tptogiar.constant.CharContant;
import com.tptogiar.constant.CharsetProperties;
import com.tptogiar.constant.http.HttpContentType;
import com.tptogiar.constant.http.HttpResponseHeader;
import com.tptogiar.servlet.context.ResponseContext;
import com.tptogiar.info.cookie.Cookie;
import com.tptogiar.info.header.Header;
import com.tptogiar.network.bio.handler.ProcessResult;
import com.tptogiar.servlet.wrapper.HttpServletRequest;
import com.tptogiar.servlet.wrapper.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;


/**
 * 组装http响应报文
 *
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 12:46:00
 */
public class HttpResponseBuilder {


    private static Logger logger = LoggerFactory.getLogger(HttpResponseBuilder.class);


    private HttpServletRequest req;

    private HttpServletResponse resp;

    private ResponseContext respContext;

    private StringBuilder headerAppender = new StringBuilder();

    private static String PROTOCOL = "HTTP/1.1";


    public HttpResponseBuilder(HttpServletRequest req, HttpServletResponse resp) {

        logger.info("封装Http响应...");
        this.req = req;
        this.resp = resp;
        this.respContext = resp.getResponseContext();
    }


    public byte[] buildResponseHeader(long length, ProcessResult processResult) throws IOException {

        appendFirstLine();

        appendContentType();

        appendHeaders();

        // TODO 添加Cookie
        appendCookies();

        // 添加Content-length响应头
        if (length == 0 && processResult.isFileTransfer()) {
            length = (int) processResult.getSrcFileChannel().size();
        }
        headerAppender.append(HttpResponseHeader.CONTENT_LENGTH).append(length);

        // 添加结束头部结束标识
        headerAppender.append(CharContant.CRLF).append(CharContant.CRLF);

        return headerAppender.toString().getBytes(CharsetProperties.UTF_8_CHARSET);
    }


    private void appendFirstLine() {
        // HTTP/1.1 200 OK\r\n
        headerAppender.append(PROTOCOL).append(CharContant.BLANK)
                .append(resp.getStatus().getCode()).append(CharContant.BLANK)
                .append(resp.getStatus()).append(CharContant.CRLF);
    }


    private void appendContentType() {

        headerAppender.append(HttpResponseHeader.CONTENT_TYPE)
                .append(resp.getContentType()).append(CharContant.CRLF);
    }


    private void appendHeaders() {

        List<Header> headers = resp.getHeaders();
        if (headers != null) {
            for (Header header : headers) {
                headerAppender.append(header.getKey()).append(CharContant.COLON).append(CharContant.BLANK)
                        .append(header.getValue()).append(CharContant.CRLF);
            }
        }
    }


    private void appendCookies() {

        List<Cookie> cookies = resp.getCookies();
        if (cookies != null) {

        }
    }


    public static byte[] combineRespHeaderAndBody(byte[] respHeaderBytes,
                                                  byte[] body) {

        // TODO 待优化
        byte[] responseBytes = new byte[respHeaderBytes.length + body.length];
        System.arraycopy(respHeaderBytes, 0, responseBytes, 0, respHeaderBytes.length);
        System.arraycopy(body, 0, responseBytes, respHeaderBytes.length, body.length);
//        logger.debug(printHttpResponseMsg(resp, respHeaderBytes, body));
        return responseBytes;
    }


    public static String printHttpResponseMsg(HttpServletResponse resp, byte[] header, byte[] body) {

        StringBuilder sb = new StringBuilder("\n================================\n" + new String(header));
        if (resp.getContentType() == HttpContentType.DEFAULT) {
            sb.append(new String(body));
        }
        sb.append("\n================================\n");
        return sb.toString();
    }


}
