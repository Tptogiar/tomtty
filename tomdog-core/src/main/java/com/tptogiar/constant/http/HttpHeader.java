package com.tptogiar.constant.http;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 13:46:00
 */
public enum HttpHeader {

    //
    CONTENT_LENGTH("Content-Length");



    private String header;

    HttpHeader(String header) {
        this.header = header;
    }
}
