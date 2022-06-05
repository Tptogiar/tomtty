import com.tptogiar.config.TomdogConfig;
import com.tptogiar.network.bio.endpoint.BioEndPoint;
import com.tptogiar.network.nio.ServerBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年04月30日 16:44:00
 */
public class TomdogStarter {

    private static Logger logger = LoggerFactory.getLogger(TomdogStarter.class);

    /**
     * 启动服务器...
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        try {
            if ("BIO".equals(TomdogConfig.ioModel.toUpperCase())){

                bioStart(TomdogConfig.serverHostname, TomdogConfig.serverPort);

            }else if ("NIO".equals(TomdogConfig.ioModel.toUpperCase())){

                nioStart(TomdogConfig.serverHostname,TomdogConfig.serverPort);

            }
            logger.info("服务器以{}模式启动完成 , 端口号为{} ",
                    TomdogConfig.ioModel,TomdogConfig.serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 以bio模式启动服务器
     * @param hostname
     * @param port
     * @throws IOException
     */
    public static void bioStart(String hostname, int port) throws IOException {
        BioEndPoint bioEndPoint = new BioEndPoint(hostname, port);
        bioEndPoint.start();
    }

    /**
     * 以nio模式启动服务器
     * @param hostname
     * @param port
     * @throws IOException
     */
    public static void nioStart(String hostname, int port) throws IOException {
        ServerBootstrap.start(TomdogConfig.serverPort, TomdogConfig.nioSubReactorCount);
    }






}
