package cn.enjoyedu.ch01.aio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * 响应网络操作的处理器
 */
public class AioServerHandler implements Runnable{

    public CountDownLatch latch;
    /*进行异步通信的通道*/
    public AsynchronousServerSocketChannel channel;

    public AioServerHandler(int port) {
        try {
            //创建服务器端通道
            channel = AsynchronousServerSocketChannel.open();
            //绑定端口
            channel.bind(new InetSocketAddress(port));
            System.out.println("server is start,port = " + port);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        channel.accept(this,new AioAcceptHandler());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
