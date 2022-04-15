package cn.enjoyedu.ch01.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO 服务器端程序
 */
public class BioServer {
    //创建服务器端socket
    private static ServerSocket serverSocket;
    //创建一个线程池，处理每个客户端的请求
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    private static void start() throws IOException{
        try {
            //通过构造函数创建serverSocket,并舰艇12345端口
            serverSocket = new ServerSocket(12345);
            System.out.println("服务器已启动，端口号是：" + 12345);
            while (true){
                Socket socket = serverSocket.accept();//用于接收新的链接
                System.out.println("有新的客户端链接-----");
                executorService.execute(new BioServerHandler(socket));
            }
        }finally {
            if(serverSocket != null){
                serverSocket.close();
            }
        }
    }

    public static void main(String[] args) throws IOException{
        start();
    }
}
