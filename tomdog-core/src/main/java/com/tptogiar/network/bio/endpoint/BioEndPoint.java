package com.tptogiar.network.bio.endpoint;


import com.tptogiar.network.bio.accept.BioAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 负责bio模式下的端到端连接
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 21:42:00
 */
public class BioEndPoint{

    protected ServerSocket socket = null;

    private Logger logger = LoggerFactory.getLogger(BioEndPoint.class);

    private BioAcceptor acceptor;


    public BioEndPoint(String  hostname, Integer port) throws IOException {
        this.acceptor = new BioAcceptor(this);
        ServerSocket socket = new ServerSocket();
        socket.bind(new InetSocketAddress(hostname,port));
        this.socket = socket;
    }



    public void start() {
        Thread thread = new Thread(acceptor);
        thread.start();
    }


    public Socket serverSocketAccept() throws IOException {
        Socket accept = socket.accept();
        return accept;
    }







}
