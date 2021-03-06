package com.tptogiar.servlet.wrapper;

import com.tptogiar.constant.http.HttpContentType;
import com.tptogiar.constant.http.HttpStatus;
import com.tptogiar.servlet.context.RequestContext;
import com.tptogiar.servlet.context.ResponseContext;
import com.tptogiar.servlet.context.impl.ResponseContextImpl;
import com.tptogiar.info.cookie.Cookie;
import com.tptogiar.info.header.Header;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * ResponseContext和RequestContext的包装类
 * 只对用户暴露context内的部分内容
 * 将response传递给用户的servlet时，传递的是这个类的示例
 *
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 13:10:00
 */
@Data
public class HttpServletResponseWrapper implements HttpServletResponse {


    private Logger logger = LoggerFactory.getLogger(HttpServletResponseWrapper.class);

    private RequestContext requestContext;

    private ResponseContext responseContext;


    private List<Header> headers;

    private List<Cookie> cookies;

    private byte[] body = new byte[0];

    private HttpStatus status = HttpStatus.OK;

    private String contentType = HttpContentType.DEFAULT;


    public HttpServletResponseWrapper(RequestContext reqContext) {

        logger.info("封装ResponseServlet...");
        this.requestContext = reqContext;
        this.responseContext = new ResponseContextImpl(reqContext);

    }


    @Override
    public void addHeader(Header header) {

        if (headers == null) {
            headers = new ArrayList<Header>();
        } else {
            headers.add(header);
        }
    }


    @Override
    public void addCookid(Cookie cookie) {

        if (cookies == null) {
            cookies = new ArrayList<>();
        } else {
            cookies.add(cookie);
        }
    }


    @Override
    public OutputStream getOutputStream() throws IOException {

        if (responseContext.getServletOutputStream() == null) {
            responseContext.createServletOutPutStream();
        }
        return responseContext.getServletOutputStream();
    }


    @Override
    public boolean hasOutPutStream() throws IOException {

        if (responseContext.getServletOutputStream() == null) {
            return false;
        }
        return true;
    }


    @Override
    public boolean isFileTransfer() {

        return responseContext.isFileTransfer();
    }


    @Override
    public void setFileTransfer(boolean fileTransfer) {

        responseContext.setFileTransfer(fileTransfer);
    }


    @Override
    public void attach(Object attachment) {

        responseContext.attach(attachment);
    }


    @Override
    public Object attachment() {

        return responseContext.getAttachment();
    }


}
