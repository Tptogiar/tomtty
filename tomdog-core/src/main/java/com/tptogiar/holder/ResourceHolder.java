package com.tptogiar.holder;

import lombok.Data;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 00:06:00
 */
@Data
public class ResourceHolder {




    private String uri;


    public ResourceHolder(String uri) {
        this.uri = uri;
    }
}
