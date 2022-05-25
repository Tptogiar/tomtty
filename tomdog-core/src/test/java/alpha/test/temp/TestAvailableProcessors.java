package alpha.test.temp;

import org.junit.Test;

/**
 * @author Tptogiar
 * @description
 * @date 2022/5/24 - 9:54
 */
public class TestAvailableProcessors {

    @Test
    public void testAvailableProcessors(){
        int i = Runtime.getRuntime().availableProcessors();
        System.out.println(i);
    }





}
