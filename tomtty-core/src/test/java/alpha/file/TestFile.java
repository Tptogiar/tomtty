package alpha.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author Tptogiar
 * @description
 * @date 2022/6/4 - 20:43
 */
public class TestFile {

    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fileInputStream =
                new FileInputStream
                        (("file:\\C:\\MyFiles\\CodeFile\\Project\\tomtty\\tomtty\\tomtty-core\\target\\classes\\default\\pages\\html\\index.html").substring(5));

        fileInputStream.getChannel();
    }

}
