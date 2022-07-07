package com.tptogiar.component.connection;

import com.tptogiar.config.TomttyConfig;
import com.tptogiar.network.nio.connection.Connection;
import com.tptogiar.network.nio.eventloop.NioEventLoop;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;
import java.util.Comparator;
import java.util.PriorityQueue;


/**
 * @author Tptogiar
 * @description
 * @date 2022/6/5 - 20:59
 */
@Data
public class ConnectionMgr implements Runnable {


    //自定义比较器，升序排列
    private static Comparator<Connection> comparator = (c1, c2) -> (int) (c1.getDeadline() - c2.getDeadline());

    private static PriorityQueue<Connection> priorityQueue = new PriorityQueue(16, comparator);

    private static Thread connectionMgrThread;

    private static Logger logger = LoggerFactory.getLogger(ConnectionMgr.class);


    private ConnectionMgr() {

    }


    public synchronized static void start() {

        if (connectionMgrThread == null) {
            connectionMgrThread = new Thread(new ConnectionMgr());
            connectionMgrThread.start();
        }
    }


    @Override
    public void run() {

        try {

            while (true) {

                handlerExpiredConnection();

                Thread.sleep(TomttyConfig.connectionMgrCheckInterval);

            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 把连接添加给ConnectionMgr进行管理，如果已经超过长连接最大数量，
     * 则返回null
     *
     * @param selectionKey
     * @param subEventLoop
     * @return
     */
    public synchronized static Connection addConnection(SelectionKey selectionKey,
                                                        NioEventLoop subEventLoop) {

        if (priorityQueue.size() >= TomttyConfig.httpKeepAliveMaxConnection) {
            return null;
        }

        long deadline = TomttyConfig.httpKeepAliveTime * 1000 + System.currentTimeMillis();
        Connection connection = new Connection(selectionKey, deadline, subEventLoop);
        if (priorityQueue.contains(connection)) {
            updateConnection(connection);
            return connection;
        }
        priorityQueue.add(connection);
        return connection;
    }


    /**
     * 处理超时的连接
     */
    private void handlerExpiredConnection() {

        if (ConnectionMgr.isEmpty()) {
            return;
        }

        while (ConnectionMgr.hasExpiredConnection()) {

            logger.info("处理超时连接...");

            ConnectionMgr.removeConnection();
        }
    }


    private static boolean hasExpiredConnection() {

        if (priorityQueue.isEmpty()) {
            return false;
        }
        Connection peek = priorityQueue.peek();
        if (peek.getDeadline() - System.currentTimeMillis() > 0) {
            return false;
        }
        return true;
    }


    private static void updateConnection(Connection connection) {

        if (!priorityQueue.contains(connection)) {
            return;
        }
        // 队列中原connection和参数connection的selectionkey是一样的，但截至时间不一样，
        // 故删掉原有的，添加新的
        priorityQueue.remove(connection);
        priorityQueue.add(connection);
    }


    private static void removeConnection() {

        Connection poll = priorityQueue.poll();
        NioEventLoop subEventLoop = poll.getSubEventLoop();
        subEventLoop.handleExpiredConnection(poll);
    }


    public synchronized static void removeConnection(Connection connection) {

        priorityQueue.remove(connection);
        NioEventLoop subEventLoop = connection.getSubEventLoop();
        subEventLoop.handleExpiredConnection(connection);
    }


    private static boolean isEmpty() {

        return priorityQueue.isEmpty();
    }


    public static boolean contains(SelectionKey selectionKey) {

        Connection connection = new Connection(selectionKey, 0, null);
        return priorityQueue.contains(connection);
    }


}


