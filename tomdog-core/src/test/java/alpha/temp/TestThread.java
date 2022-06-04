package alpha.temp;

/**
 * @author Tptogiar
 * @Description
 * @createTime 2022年05月27日 15:54:00
 */
public class TestThread extends Thread {

    @Override
    public void run() {
        System.out.println("run thread ...");
        System.out.println(Thread.currentThread().getName());
    }


    public static void main(String[] args) {
        TestThread testThread = new TestThread();
        testThread.start();

    }
}
