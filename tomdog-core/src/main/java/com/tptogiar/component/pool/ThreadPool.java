package com.tptogiar.component.pool;


import com.tptogiar.network.HttpHandler;
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
public class ThreadPool {

    private static Logger logger = LoggerFactory.getLogger(ThreadPool.class);

    private static ThreadPoolExecutor poolExecutor;

    static {
        poolExecutor = new ThreadPoolExecutor(
                5,1000,1000,
                TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5));


        poolExecutor.setRejectedExecutionHandler(new RejectPolicyHandler());


    }

    public static void execute(HttpHandler httpHandler){
        try{
            logger.warn(poolExecutor.toString());
            logger.info("线程池进行任务调度...");
            poolExecutor.execute(httpHandler);
        }catch (Exception e){
            e.printStackTrace();
        }

    }





}


