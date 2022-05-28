package com.tptogiar.network.nio;

import com.tptogiar.network.nio.reactor.NioEnventLoop;
import com.tptogiar.network.nio.reactor.NioEventLoopGroup;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 16:38:00
 */
public class ServerBootstrap {



    static void start(int port,int childLoopCount) throws IOException {
        NioEventLoopGroup eventLoopGroup = NioEventLoopGroup.createEventLoopGroup(childLoopCount);
        NioEnventLoop mainEventLoop = NioEnventLoop.createMainEventLoop(port, eventLoopGroup);
        mainEventLoop.start();


    }


    public static void main(String[] args) throws IOException {
        start(8848,5);
    }
    
}
