package alpha.connection.mgr;

import com.tptogiar.component.connection.ConnectionMgr;
import com.tptogiar.network.nio.connection.Connection;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Tptogiar
 * @description
 * @date 2022/6/5 - 21:50
 */
public class TestConnectionMgr {

    @Test
    public void testConnectionMgr() throws InterruptedException {
        ConnectionMgr connectionMgr = new ConnectionMgr(null);


        for (int i = 0; i < 10; i++) {
            connectionMgr.addConnection(null);
            Thread.sleep(100);
        }

        Object[] objects = connectionMgr.getPriorityQueue().toArray();
        System.out.println(connectionMgr.getPriorityQueue().size());
        for (Object object : objects) {
            System.out.println(object);
        }

        System.out.println("---");
        System.out.println(connectionMgr.pollConnection());
        System.out.println("---");

        Object[] ojbs = connectionMgr.getPriorityQueue().toArray();
        System.out.println(connectionMgr.getPriorityQueue().size());
        for (Object object : objects) {
            System.out.println(object);
        }



    }







}
