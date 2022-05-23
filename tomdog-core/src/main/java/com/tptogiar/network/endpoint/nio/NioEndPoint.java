package com.tptogiar.network.endpoint.nio;

import com.tptogiar.network.accept.nio.NioAcceptor;
import com.tptogiar.network.endpoint.EndPoint;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 21:43:00
 */
public class NioEndPoint extends EndPoint {


    private NioAcceptor acceptor;

    public NioEndPoint(String hostname, int port) {
        NioAcceptor acceptor = new NioAcceptor(this);

//        super();
    }

    @Override
    public void start() {
        Thread thread = new Thread((Runnable) acceptor);
        thread.start();
    }

    @Override
    public Socket serverSocketAccept() throws IOException {
        Socket accept = socket.accept();
        return accept;
    }
}
