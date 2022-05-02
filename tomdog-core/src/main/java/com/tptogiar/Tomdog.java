package com.tptogiar;

import com.tptogiar.config.TomdogConfig;
import com.tptogiar.network.endpoint.bio.BioEndPoint;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 16:44:00
 */
public class Tomdog {


    public static void start(String[] args) throws IOException {
        BioEndPoint bioEndPoint =
                new BioEndPoint(TomdogConfig.SERVER_HOSTNAME, TomdogConfig.SERVER_PORT);

        bioEndPoint.start();

    }









}
