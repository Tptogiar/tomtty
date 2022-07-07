package com.tptogiar.network.nio.poller;

import com.tptogiar.component.connection.ConnectionMgr;
import com.tptogiar.component.queue.LinkedDoubleBufferQueue;
import com.tptogiar.network.nio.eventloop.NioEventLoop;
import com.tptogiar.network.nio.handler.tcp.NioTCPHandler;
import com.tptogiar.network.nio.task.EventTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 22:21:00
 */
public class SubPoller implements Poller {


    Logger logger = LoggerFactory.getLogger(SubPoller.class);

    private NioEventLoop subEventLoop;

    private Selector subSelector;

    private NioTCPHandler nioTcpHandler;

    private AtomicBoolean running;

    private LinkedDoubleBufferQueue<EventTask> eventQueue;




    public SubPoller(NioEventLoop subEventLoop) {

        this.subEventLoop = subEventLoop;
        subSelector = subEventLoop.getSelector();
        nioTcpHandler = new NioTCPHandler(subEventLoop);
        running = subEventLoop.getRunning();
        eventQueue = subEventLoop.getEventQueue();

    }


    /**
     * 轮询
     *
     * @throws Exception
     */
    @Override
    public void poll() throws Exception {

        while (running.get()) {

            logger.info(subEventLoop + " poll()");

            // 先处理任务队列里面的任务（如：有新的client要注册到该subSelector等）
            executeTask();

            doSelect();

            scanSelectionKey();

        }
    }


    private void executeTask() throws InterruptedException {

        int taskQueueSize = eventQueue.size();
        if (taskQueueSize == 0) {
            return;
        }
        logger.info(subEventLoop + "处理任务队列, eventQueue.size:" + eventQueue.size());
        EventTask eventTask = eventQueue.poll();
        while (eventTask != null) {
            eventTask.getRunnable().run();
            eventTask = eventQueue.poll();
        }
        logger.info(subEventLoop + "任务队列处理完毕...");
    }


    private void doSelect() throws IOException {

        logger.info(subEventLoop + "执行select...");
        // 把select标识位置为true，以便在mainReactor线程可以根据该标志为决定是否进行selector.wakeup()
        subEventLoop.getSelecting().set(true);
        int select = subEventLoop.select();
        subEventLoop.getSelecting().set(false);
        logger.info(subEventLoop + "select返回,返回值为{}...", select);
    }


    private void scanSelectionKey() throws IOException {

        try {
            Set<SelectionKey> selectionKeys = subSelector.selectedKeys();
            logger.info("在Selector:{}上开始遍历selectionKey...", subSelector.hashCode());
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            logger.info("selectionKeys大小为：{}", selectionKeys.size());
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (!selectionKey.isValid()) {
                    continue;
                }

                if (selectionKey.isReadable()) {
                    nioTcpHandler.read(selectionKey);

                } else if (selectionKey.isWritable()) {
                    nioTcpHandler.write(selectionKey);
                }

            }
            logger.info("在Selector:{}上结束遍历selectionKey...", subSelector.hashCode());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(subEventLoop);
            running.set(false);
        }

    }


}
