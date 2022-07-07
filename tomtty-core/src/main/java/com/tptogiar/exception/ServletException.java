package com.tptogiar.exception;

import com.tptogiar.constant.http.HttpStatus;


/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 13:32:00
 */
public class ServletException extends Exception {


    private HttpStatus httpStatus;

    private String message;


    public ServletException(HttpStatus httpStatus) {

        this.httpStatus = httpStatus;
    }


    public ServletException(HttpStatus httpStatus, String message) {

        this.httpStatus = httpStatus;
        this.message = message;
    }


}
