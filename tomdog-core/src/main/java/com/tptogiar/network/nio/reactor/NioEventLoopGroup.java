package com.tptogiar.network.nio.reactor;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 16:32:00
 */
public class NioEventLoopGroup {

    private NioEnventLoop[] enventLoops;

    private int subEventLoopIndex;


    public NioEventLoopGroup(NioEnventLoop[] enventLoops) {
        this.enventLoops = enventLoops;
    }

    public static NioEventLoopGroup createEventLoopGroup(int childLoopCount) throws IOException {
        NioEnventLoop[] enventLoops = new NioEnventLoop[childLoopCount];
        for (int i = 0; i < childLoopCount; i++) {
            enventLoops[i] = NioEnventLoop.createSubEventLoop();
        }
        return new NioEventLoopGroup(enventLoops);
    }


    public NioEnventLoop getEventLoop() {
        NioEnventLoop enventLoop = enventLoops[(subEventLoopIndex % enventLoops.length)];
        subEventLoopIndex++;
        return enventLoop;
    }




}
