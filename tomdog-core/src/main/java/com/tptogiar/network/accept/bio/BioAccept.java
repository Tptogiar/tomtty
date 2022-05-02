package com.tptogiar.network.accept.bio;

import com.tptogiar.network.endpoint.bio.BioEndPoint;
import com.tptogiar.network.handler.bio.BioHttpHandler;
import com.tptogiar.component.Poller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 21:46:00
 */
public class BioAccept implements Runnable{

    private Logger logger = LoggerFactory.getLogger(BioAccept.class);

    private BioEndPoint endPoint;

    public BioAccept(BioEndPoint endPoint) {
        this.endPoint = endPoint;
    }

    @Override
    public void run() {
        try {
            while (true){
                logger.info("等待fd...");
                Socket socket = endPoint.serverSocketAccept();
                logger.info("Accept了一个fd...");
                BioHttpHandler bioHttpHandler = new BioHttpHandler(socket);
                Poller.execute(bioHttpHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
