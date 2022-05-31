import com.tptogiar.Tomdog;
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

    private static  Logger logger = LoggerFactory.getLogger(Starter.class);

    /**
     * 启动服务器...
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        try {
//        Tomdog.start(TomdogConfig.SERVER_HOSTNAME,TomdogConfig.SERVER_PORT);

            ServerBootstrap.start(TomdogConfig.SERVER_PORT,TomdogConfig.SERVER_NIO_SUB_REACTOR_COUNT);
            logger.info(Thread.currentThread().getName());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }



}
