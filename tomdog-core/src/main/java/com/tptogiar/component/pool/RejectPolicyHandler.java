package com.tptogiar.component.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月05日 12:54:00
 */
public class RejectPolicyHandler implements RejectedExecutionHandler {

    private Logger logger = LoggerFactory.getLogger(RejectPolicyHandler.class);

    private static int rejectCount = 0;


    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        rejectCount++;
        logger.warn("current reject count = {}",rejectCount);

    }
}
