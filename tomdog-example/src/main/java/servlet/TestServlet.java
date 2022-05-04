package servlet;

import com.tptogiar.temp.HttpServletRequest;
import com.tptogiar.temp.HttpServletRequestWrapper;
import com.tptogiar.temp.HttpServletResponse;
import com.tptogiar.temp.HttpServletResponseWrapper;
import com.tptogiar.servlet.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 10:43:00
 */
public class TestServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(TestServlet.class);


    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("do service ----From TestServlet...");
        OutputStream outPutStream = resp.getOutPutStream();
        String jsonStr = "<h1 style='color:red'>do service success</h1>";
        outPutStream.write(jsonStr.getBytes());
    }
}
