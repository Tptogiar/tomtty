package servlet;

import com.tptogiar.servlet.HttpRequestServlet;
import com.tptogiar.servlet.HttpResponseServlet;
import com.tptogiar.servlet.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 10:43:00
 */
public class TestServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(TestServlet.class);


    @Override
    public void service(HttpRequestServlet req, HttpResponseServlet resp) {
        logger.info("do service ----From TestServlet...");
    }
}
