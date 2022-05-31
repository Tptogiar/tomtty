package com.tptogiar.network.nio.poller;

import com.tptogiar.network.nio.handler.TCPHandler;
import com.tptogiar.network.nio.eventloop.NioEnventLoop;
import com.tptogiar.network.nio.task.EventTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 22:21:00
 */
public class SubPoller implements Poller {

    Logger logger = LoggerFactory.getLogger(SubPoller.class);

    private NioEnventLoop subEventLoop;

    private Selector subSelector;

    private TCPHandler tcpHandler;

    private AtomicBoolean running;

    private BlockingQueue<EventTask> eventQueue;


    public SubPoller(NioEnventLoop subEventLoop) {
        this.subEventLoop = subEventLoop;
        subSelector = subEventLoop.getSelector();
        tcpHandler = new TCPHandler(subEventLoop);
        running = subEventLoop.getRunning();
        eventQueue = subEventLoop.getEventQueue();
    }


    @Override
    public void poll() {
        try {
            while (running.get()) {
                logger.info(subEventLoop+" 新一轮poll()");
                // 先处理任务队列里面的任务（如：有新的client要注册到该subSelector等）
                executeTask();

                doSelect();

                scanSelectionKey();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void executeTask() throws InterruptedException {
        logger.info(subEventLoop + "处理任务队列, eventQueue.size:" + eventQueue.size());
        EventTask eventTask = eventQueue.poll(1000, TimeUnit.MILLISECONDS);
        while (eventTask!=null){
            eventTask.getRunnable().run();
            eventTask = eventQueue.poll(1000, TimeUnit.MILLISECONDS);
        }
        logger.info(subEventLoop + "任务队列处理完毕...");
    }


    private void doSelect() throws IOException {
        logger.info(subEventLoop+"执行select...");
        // 把select标识位置为true，以便在mainReactor线程可以根据该标志为决定是否进行selector.wakeup()
        subEventLoop.getSelecting().set(true);
        int select = subEventLoop.select();
        subEventLoop.getSelecting().set(false);
        logger.info(subEventLoop+"select返回...");
    }


    private void scanSelectionKey() throws IOException {

        Set<SelectionKey> selectionKeys = subSelector.selectedKeys();
        logger.info("在Selector:{}上开始遍历selectionKey...",subSelector.hashCode());
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = null;
            selectionKey = iterator.next();
            iterator.remove();
            if (!selectionKey.isValid()) {
                continue;
            }

            if (selectionKey.isReadable()) {
                tcpHandler.read(selectionKey);
            } else if (selectionKey.isWritable()) {
                tcpHandler.write(selectionKey);
            }
        }
        logger.info("在Selector:{}上结束遍历selectionKey...",subSelector.hashCode());

    }


}
