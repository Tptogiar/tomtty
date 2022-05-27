//package com.tptogiar.network.bio.endpoint.nio;
//
//import com.tptogiar.network.bio.accept.nio.NioAcceptor;
//
//import java.io.IOException;
//import java.net.Socket;
//
///**
// * @author Tptogiar
// * @Description
// * @createTime 2022年04月30日 21:43:00
// */
//public class NioEndPoint {
//
//
//    private NioAcceptor acceptor;
//
//    public NioEndPoint(String hostname, int port) {
//        NioAcceptor acceptor = new NioAcceptor(this);
//
////        super();
//    }
//
//
//    public void start() {
//        Thread thread = new Thread((Runnable) acceptor);
//        thread.start();
//    }
//
//
//    public Socket serverSocketAccept() throws IOException {
//        Socket accept = socket.accept();
//        return accept;
//    }
//}
