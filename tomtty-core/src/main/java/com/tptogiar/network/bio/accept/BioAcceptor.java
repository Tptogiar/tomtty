package com.tptogiar.network.bio.accept;

import com.tptogiar.component.pool.WorkerThreadPool;
import com.tptogiar.network.bio.endpoint.BioEndPoint;
import com.tptogiar.network.bio.handler.BioHttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;


/**
 * 负责accept新连接
 *
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 21:46:00
 */
public class BioAcceptor implements Runnable {


    private Logger logger = LoggerFactory.getLogger(BioAcceptor.class);

    private BioEndPoint endPoint;


    public BioAcceptor(BioEndPoint endPoint) {

        this.endPoint = endPoint;
    }


    @Override
    public void run() {

        try {
            while (true) {
                logger.info("等待客户端连接...");
                Socket socket = endPoint.serverSocketAccept();
                logger.info("接收到客户端连接，accept了一个fd...");
                BioHttpHandler bioHttpHandler = new BioHttpHandler(socket);
                WorkerThreadPool.execute(bioHttpHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
