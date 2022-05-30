package com.tptogiar.network.nio;

import com.tptogiar.network.nio.eventloop.NioEnventLoop;
import com.tptogiar.network.nio.eventloop.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 16:38:00
 */
public class ServerBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(ServerBootstrap.class);

    public static void start(int port,int childLoopCount) throws IOException {
        NioEventLoopGroup eventLoopGroup = NioEventLoopGroup.createEventLoopGroup(childLoopCount);
        NioEnventLoop mainEventLoop = NioEnventLoop.createMainEventLoop(port, eventLoopGroup);
        mainEventLoop.setName(mainEventLoop.toString());
        logger.info("MainEventLoop及EventLoopGroup启动完成...");
        mainEventLoop.start();
    }
    
}
