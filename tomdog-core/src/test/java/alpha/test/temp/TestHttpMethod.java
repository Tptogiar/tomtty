package alpha.test.temp;

import com.tptogiar.constant.http.HttpMethod;
import org.junit.Test;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月01日 17:29:00
 */
public class TestHttpMethod {


    @Test
    public void testHttpMethodEnum(){

        HttpMethod get = HttpMethod.valueOf("GET");
        System.out.println(get);


    }



}
