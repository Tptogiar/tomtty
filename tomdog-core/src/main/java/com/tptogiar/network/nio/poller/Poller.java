package com.tptogiar.network.nio.poller;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 22:22:00
 */
public interface Poller{


    void poll() throws IOException;



}
