import com.tptogiar.config.TomttyConfig;
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
public class TomttyStarter {


    private static Logger logger = LoggerFactory.getLogger(TomttyStarter.class);


    /**
     * 启动服务器...
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        switchIoModel2Start();

    }


    /**
     * 根据配置，选择启动对应的IO模式
     */
    public static void switchIoModel2Start() {

        try {
            String ioModel = TomttyConfig.ioModel.toUpperCase();

            if ("BIO".equals(ioModel)) {
                bioStart(TomttyConfig.serverHostname, TomttyConfig.serverPort);
            } else if ("NIO".equals(ioModel)) {
                nioStart(TomttyConfig.serverHostname, TomttyConfig.serverPort);
            }

            logger.info("服务器以{}模式启动完成 , 端口号为{} ", TomttyConfig.ioModel,
                        TomttyConfig.serverPort);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 以bio模式启动服务器
     *
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
     *
     * @param hostname
     * @param port
     * @throws IOException
     */
    public static void nioStart(String hostname, int port) throws IOException {

        ServerBootstrap.start(TomttyConfig.serverPort, TomttyConfig.nioSubReactorCount);
    }


}
