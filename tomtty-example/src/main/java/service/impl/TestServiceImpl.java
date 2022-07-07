package service.impl;

import dao.TestDao;
import dao.impl.TestDaoImpl;
import service.TestService;


/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年07月03日 13:21:00
 */
public class TestServiceImpl implements TestService {

    private TestDao testDao = new TestDaoImpl();


    @Override
    public void test() {

        System.out.println("Test TestService...");
        testDao.test();
    }


}
