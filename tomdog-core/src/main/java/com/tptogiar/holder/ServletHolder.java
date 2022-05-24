package com.tptogiar.holder;

import com.tptogiar.servlet.Servlet;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 封装servlet信息
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 18:00:00
 */
@Data
@AllArgsConstructor
public class ServletHolder {

    private String uri;
    private Class sevletClass;
    private Servlet servlet;
    private String servletName;


    public ServletHolder(Class sevletClass, String servletName) {
        this.sevletClass = sevletClass;
        this.servletName = servletName;
    }
}
