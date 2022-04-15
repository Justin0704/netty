package cn.enjoyedu.ch01.aio.client;

import java.util.Scanner;

import static cn.enjoyedu.ch01.Constants.DEFAULT_PORT;
import static cn.enjoyedu.ch01.Constants.DEFAULT_SERVER_IP;

/**
 * aio客户端程序
 */
public class AioClient {

    //IO通信处理器
    private static AioClientHandler clientHandle;

    public static void start(){
        if(clientHandle!=null)
            return;
        clientHandle = new AioClientHandler(DEFAULT_SERVER_IP,DEFAULT_PORT);
        //负责网络通讯的线程
        new Thread(clientHandle,"Client").start();
    }
    //向服务器发送消息
    public static boolean sendMsg(String msg) throws Exception{
        if(msg.equals("q")) return false;
        clientHandle.sendMessage(msg);
        return true;
    }
    public static void main(String[] args) throws Exception{
        AioClient.start();
        System.out.println("请输入请求消息：");
        Scanner scanner = new Scanner(System.in);
        while(AioClient.sendMsg(scanner.nextLine()));
    }
}
