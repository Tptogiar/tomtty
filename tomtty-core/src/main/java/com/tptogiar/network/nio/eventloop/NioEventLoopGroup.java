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

    private NioEventLoop[] enventLoops;

    private int subEventLoopIndex;


    public NioEventLoopGroup(NioEventLoop[] enventLoops) {
        this.enventLoops = enventLoops;
    }


    /**
     * 创建指定个数的subReactor
     * @param childLoopCount
     * @return
     * @throws IOException
     */
    public static NioEventLoopGroup createEventLoopGroup(int childLoopCount) throws IOException {
        logger.info("初始化EventLoopGroup...");
        NioEventLoop[] enventLoops = new NioEventLoop[childLoopCount];
        for (int i = 0; i < childLoopCount; i++) {
            enventLoops[i] = NioEventLoop.createSubEventLoop(i);
            enventLoops[i].setName(enventLoops[i].toString());
            enventLoops[i].start();
        }
        return new NioEventLoopGroup(enventLoops);
    }


    /**
     * 轮询实现负载均衡
     * @return
     */
    public NioEventLoop getEventLoop() {
        NioEventLoop enventLoop = enventLoops[(subEventLoopIndex % enventLoops.length)];
        subEventLoopIndex++;
        return enventLoop;
    }


}
