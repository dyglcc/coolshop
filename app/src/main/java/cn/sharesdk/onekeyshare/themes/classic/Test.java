package cn.sharesdk.onekeyshare.themes.classic;

import java.io.File;

/**
 * Created by dongyuangui on 2018/5/3.
 */

public class Test {
    public static void main(String[] args) {

        int count = 0;
        File file = new File("/Users/dongyuangui/Desktop/apptimize/com/apptimize");
//        File file = new File("/Users/dongyuangui/Desktop/testin/cn/testin/analysis");
        for (File file1 : file.listFiles()) {
            count++;
        }
        System.out.println(count);
        System.out.println(file.listFiles().length);
    }
}
