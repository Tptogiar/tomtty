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
    public void poll() throws IOException, InterruptedException {
        while (running.get()) {

            // 先处理任务队列里面的任务（如：有新的client要注册到该subSelector等）
            executeTask();

            doSelect();

            scanSelectionKey();

        }
    }


    private void executeTask() throws InterruptedException {
        EventTask eventTask = eventQueue.poll(1000, TimeUnit.MILLISECONDS);
        if (eventTask==null){
            return;
        }
        eventTask.getRunnable().run();
    }


    private void doSelect() throws IOException {
        // 把select标识位置为true，以便在mainReactor线程可以根据该标志为决定是否进行selector.wakeup()
        subEventLoop.getSelecting().set(true);
        int select = subEventLoop.select();
        subEventLoop.getSelecting().set(false);
    }


    private void scanSelectionKey() throws IOException {
        Set<SelectionKey> selectionKeys = subSelector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
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
    }


}
