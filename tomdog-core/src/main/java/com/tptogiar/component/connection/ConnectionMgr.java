package com.tptogiar.component.connection;

import com.tptogiar.config.TomdogConfig;
import com.tptogiar.network.nio.connection.Connection;
import com.tptogiar.network.nio.eventloop.NioEventLoop;
import lombok.Data;

import java.nio.channels.SelectionKey;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * @author Tptogiar
 * @description
 * @date 2022/6/5 - 20:59
 */
@Data
public class ConnectionMgr {


    //自定义比较器，升序排列
    private static Comparator<Connection> comparator = comparator = (c1, c2) -> (int) (c1.getDeadline()-c2.getDeadline());

    private PriorityQueue<Connection> priorityQueue = new PriorityQueue(16,comparator);

    private NioEventLoop subEvnetLoop;

    public ConnectionMgr(NioEventLoop nioEnventLoop) {
        subEvnetLoop = nioEnventLoop;
    }

    public Connection addConnection(SelectionKey selectionKey){
        long deadline = TomdogConfig.httpKeepAliveTime*1000 + System.currentTimeMillis();
        Connection connection = new Connection(selectionKey, deadline);
        if (priorityQueue.contains(connection)){
            return connection;
        }
        priorityQueue.add(connection);
        return connection;
    }

    public boolean hasExpiredConnection(){
        if (priorityQueue.isEmpty()){
            return false;
        }
        Connection peek = priorityQueue.peek();
        if (peek.getDeadline()-System.currentTimeMillis()>0){
            return false;
        }
        return true;
    }

    public void updateConnection(Connection connection){
        if (! priorityQueue.contains(connection)){
            return;
        }
        priorityQueue.remove(connection);
        priorityQueue.add(connection);
    }

    public void removeConnection() {
        Connection poll = priorityQueue.poll();
        subEvnetLoop.handleExpiredConnection(poll);
    }


    public Connection pollConnection(){
        return priorityQueue.poll();
    }

    public boolean isEmpty(){
        return priorityQueue.isEmpty();
    }

    public Connection peekConnection(){
        return priorityQueue.peek();
    }



}


