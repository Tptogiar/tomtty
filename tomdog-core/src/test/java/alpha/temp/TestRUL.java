package alpha.temp;

import com.tptogiar.component.dispatch.ServletDispatcher;
import org.junit.Test;

import java.net.URL;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月02日 23:30:00
 */
public class TestRUL {


    @Test
    public void testRUL() {
        URL resource = ServletDispatcher.class.getResource("/sdfsdf.xml");
        System.out.println(resource);
    }

}
