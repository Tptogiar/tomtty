package com.tptogiar.network.nio.eventloop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 16:32:00
 */
public class NioEventLoopGroup {

    private static final Logger logger = LoggerFactory.getLogger(NioEventLoopGroup.class);

    private NioEnventLoop[] enventLoops;

    private int subEventLoopIndex;


    public NioEventLoopGroup(NioEnventLoop[] enventLoops) {
        this.enventLoops = enventLoops;
    }

    public static NioEventLoopGroup createEventLoopGroup(int childLoopCount) throws IOException {
        logger.info("初始化EventLoopGroup...");
        NioEnventLoop[] enventLoops = new NioEnventLoop[childLoopCount];
        for (int i = 0; i < childLoopCount; i++) {
            enventLoops[i] = NioEnventLoop.createSubEventLoop(i);
            enventLoops[i].setName(enventLoops[i].toString());
            enventLoops[i].start();
        }
        return new NioEventLoopGroup(enventLoops);
    }


    public NioEnventLoop getEventLoop() {
        NioEnventLoop enventLoop = enventLoops[(subEventLoopIndex % enventLoops.length)];
        subEventLoopIndex++;
        return enventLoop;
    }




}
