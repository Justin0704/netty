package cn.enjoyedu.ch01.aio.server;

/**
 * 服务器端程序
 */
public class AioServer {

    private static AioServerHandler aioServerHandler;

    public volatile static long clientCount = 0;

    public static void start(){
        if(aioServerHandler!=null)
            return;
        aioServerHandler = new AioServerHandler(12345);
        new Thread(aioServerHandler,"Server").start();
    }
    public static void main(String[] args){
        AioServer.start();
    }
}
