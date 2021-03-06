package servlet;

import com.tptogiar.servlet.wrapper.HttpServletRequest;
import com.tptogiar.servlet.wrapper.HttpServletResponse;
import com.tptogiar.servlet.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.TestService;
import service.impl.TestServiceImpl;

import java.io.IOException;
import java.io.OutputStream;


/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 10:43:00
 */
public class TestServlet extends HttpServlet {


    private Logger logger = LoggerFactory.getLogger(TestServlet.class);

    private TestService testService = new TestServiceImpl();


    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("do service ----From TestServlet...");
        OutputStream outPutStream = resp.getOutputStream();
        String jsonStr = "<h1 style='color:red'>do service success</h1>";
        testService.test();
        outPutStream.write(jsonStr.getBytes());
    }


}
