package com.tptogiar.temp;

import com.tptogiar.constant.http.HttpContentType;
import com.tptogiar.constant.http.HttpStatus;
import com.tptogiar.context.RequestContext;
import com.tptogiar.context.ResponseContext;
import com.tptogiar.context.impl.ResponseContextImpl;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 13:10:00
 */
@Data
public class HttpServletResponseWrapper implements HttpServletResponse{

    private Logger logger = LoggerFactory.getLogger(HttpServletResponseWrapper.class);

    private RequestContext requestContext;

    private ResponseContext responseContext;

    private ServletOutputStream servletOutputStream;


    private List<Header> headers;
    private List<Cookie> cookies;

    private byte[] body =new byte[0];

    private HttpStatus status = HttpStatus.OK;
    private String contentType = HttpContentType.DEFAULT;



    public HttpServletResponseWrapper(RequestContext reqContext) {
        logger.info("封装ResponseServlet...");
        this.requestContext = reqContext;
        this.responseContext = new ResponseContextImpl(reqContext);

    }


    @Override
    public void addHeader(Header header) {
        if (headers==null){
            headers =new ArrayList<Header>();
        }
        else{
            headers.add(header);
        }
    }

    @Override
    public void addCookid(Cookie cookie) {
        if (cookies==null){
            cookies = new ArrayList<>();
        }
        else {
            cookies.add(cookie);
        }
    }



    @Override
    public OutputStream getOutPutStream() throws IOException {
        if (servletOutputStream==null){
            servletOutputStream = new ServletOutputStreamImpl();
        }
        return (OutputStream) servletOutputStream;
    }


}
