import com.tptogiar.config.TomdogConfig;
import com.tptogiar.network.nio.ServerBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 23:57:00
 */
public class Starter {

    private static Logger logger = LoggerFactory.getLogger(Starter.class);

    /**
     * 启动服务器...
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
//        TomdogConfig.THREAD_POOL_CORE_POOL_SIZE = Integer.valueOf(args[0]);
//        TomdogConfig.THREAD_POOL_MAXIMUM_POOL_SIZE = Integer.valueOf(args[1]);


        try {
//            Tomdog.start(TomdogConfig.SERVER_HOSTNAME, TomdogConfig.SERVER_PORT);

            ServerBootstrap.start(TomdogConfig.serverPort, TomdogConfig.nioSubReactorCount);
            logger.info("服务器启动完成...,端口号为{}",TomdogConfig.serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
