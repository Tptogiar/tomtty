package com.tptogiar.exception;

import com.tptogiar.constant.http.HttpStatus;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 13:39:00
 */
public class RequestInvaildException extends ServletException{
    private static final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    public RequestInvaildException() {
        super(httpStatus);
    }

    public RequestInvaildException(String message) {
        super(httpStatus,message);
    }

}
