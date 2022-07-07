package com.tptogiar.constant.http;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 13:33:00
 */
public enum HttpStatus {

    //
    OK(200),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    BAD_REQUEST(400);


    private int code;


    HttpStatus(int code) {

        this.code = code;
    }


    public int getCode() {

        return code;
    }
}
