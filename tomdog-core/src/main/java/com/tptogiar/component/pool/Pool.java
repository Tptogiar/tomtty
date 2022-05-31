package com.tptogiar.component.pool;


import com.tptogiar.config.TomdogConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 22:26:00
 */
public class Pool {

    private static Logger logger = LoggerFactory.getLogger(Pool.class);

    private static ThreadPoolExecutor poolExecutor;

    static {
        poolExecutor = new ThreadPoolExecutor(
                TomdogConfig.THREAD_POOL_CORE_POOL_SIZE,
                TomdogConfig.THREAD_POOL_MAXIMUM_POOL_SIZE,
                TomdogConfig.THREAD_POOL_KEEP_ALIVE_TIME ,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(TomdogConfig.THTEAD_POOL_BLOCKING_QUEUE_SIZE));


        poolExecutor.setRejectedExecutionHandler(new RejectPolicyHandler());


    }

    public static void execute(Runnable task) {
        try {
            logger.debug(poolExecutor.toString());
            logger.info("线程池进行任务调度...");
            poolExecutor.execute(task);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void submit(Runnable task) {
        logger.debug(poolExecutor.toString());
        Future<?> result = poolExecutor.submit(task);

    }


}


