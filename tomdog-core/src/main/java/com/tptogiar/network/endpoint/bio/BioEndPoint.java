package com.tptogiar.network.endpoint.bio;


import com.tptogiar.network.accept.bio.BioAccept;
import com.tptogiar.network.handler.bio.BioHttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 21:42:00
 */
public class BioEndPoint {

    private Logger logger = LoggerFactory.getLogger(BioEndPoint.class);

    private  BioAccept accept;
    private  ServerSocket socket = null;

    public BioEndPoint(String  hostname, Integer port) throws IOException {
        this.accept = new BioAccept(this);
        ServerSocket socket = new ServerSocket();
        socket.bind(new InetSocketAddress(hostname,port));
        this.socket = socket;
    }




    public void start() {
        Thread thread = new Thread(accept);
//        thread.setDaemon(true);
        thread.start();
    }

    public Socket serverSocketAccept() throws IOException {
        Socket accept = socket.accept();
        return accept;
    }







}
