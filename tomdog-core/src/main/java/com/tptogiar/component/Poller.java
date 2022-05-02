package com.tptogiar.component;

import com.tptogiar.network.handler.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 22:26:00
 */
public class Poller {

    private static Logger logger = LoggerFactory.getLogger(Poller.class);

    private static ThreadPoolExecutor poolExecutor;

    static {
        poolExecutor = new ThreadPoolExecutor(
                5,10,1000,
                TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5));
    }

    public static void execute(HttpHandler httpHandler){
        logger.info("线程池进行任务调度...");
        poolExecutor.execute(httpHandler);

    }





}
