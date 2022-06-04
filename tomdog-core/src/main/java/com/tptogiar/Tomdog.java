package com.tptogiar;

import com.tptogiar.network.bio.endpoint.BioEndPoint;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 16:44:00
 */
public class Tomdog {


    public static void start(String hostname, int port) throws IOException {
        BioEndPoint bioEndPoint = new BioEndPoint(hostname, port);
        bioEndPoint.start();
    }


}
