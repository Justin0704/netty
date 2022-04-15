package cn.enjoyedu.ch01;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {
    public static int DEFAULT_PORT = 12345;
    public static String DEFAULT_SERVER_IP = "127.0.0.1";

    public static String response(String msg){
        return "hello," + msg + ",now is " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
