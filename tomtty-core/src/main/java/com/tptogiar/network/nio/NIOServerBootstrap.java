package com.tptogiar.network.nio;


import com.tptogiar.component.connection.ConnectionMgr;
import com.tptogiar.network.nio.eventloop.NioEventLoop;
import com.tptogiar.network.nio.eventloop.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 16:38:00
 */
public class NIOServerBootstrap {


    private static final Logger logger = LoggerFactory.getLogger(NIOServerBootstrap.class);


    public static void start(int port, int childLoopCount) throws IOException {

        NioEventLoopGroup eventLoopGroup = NioEventLoopGroup.createEventLoopGroup(childLoopCount);

        NioEventLoop.createMainEventLoop(port, eventLoopGroup);

        ConnectionMgr.start();

        logger.info("MainEventLoop,{}个EventLoopGroup,及定时器启动完成...", childLoopCount);
    }


}
