package cn.enjoyedu.ch01.aio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * IO通信处理器，负责连接服务器，对外暴露对服务端发送数据的API
 */
public class AioClientHandler implements CompletionHandler<Void,AioClientHandler>,Runnable{

    private AsynchronousSocketChannel clientChannel;
    private String host;
    private int port;

    private CountDownLatch latch;//防止线程退出

    public AioClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            //创建一个实际异步的客户端通道
            clientChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //创建CountDownLatch,由于是异步调用，下面connect不会阻塞
        //那么整个run方法会迅速结束，那么负责网络通讯的线程也会迅速结束
        latch = new CountDownLatch(1);
        //发起异步链接操作，毁掉参数就是这个实例本身
        //如果连接成功，会回调这个实例的complete方法
        clientChannel.connect(new InetSocketAddress(host,port),null,this);
        try {
            latch.await();
            clientChannel.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AioClientHandler attachment) {
        System.out.println("已经连接到服务器端");
    }

    @Override
    public void failed(Throwable exc, AioClientHandler attachment) {
        System.err.println("连接失败。");
        exc.printStackTrace();
        latch.countDown();
        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //对外暴露对服务器端发送数据的api
    public void sendMessage(String msg){
        //为了把msg变成可以再网络传输的格式
        byte[] bytes = msg.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();

        clientChannel.write(writeBuffer,writeBuffer,new AioClientWriteHandler(clientChannel,latch));
    }
}
