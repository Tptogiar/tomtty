package com.tptogiar.network.accept.nio;

import com.tptogiar.network.endpoint.nio.NioEndPoint;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 21:46:00
 */
public class NioAcceptor {

    private NioEndPoint endPoint;


    public NioAcceptor(NioEndPoint nioEndPoint) {
        endPoint = nioEndPoint;
    }
}
