package com.tptogiar.network.endpoint;

import com.tptogiar.Tomdog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** 负责端到端的socket连接
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 21:41:00
 */
public abstract class EndPoint {

    protected ServerSocket socket = null;

    /**
     * 启动endpoint
     */
    public abstract void start();

    /**
     * accept一个新连接
     * @return
     * @throws IOException
     */
    public abstract Socket serverSocketAccept() throws IOException;




}
