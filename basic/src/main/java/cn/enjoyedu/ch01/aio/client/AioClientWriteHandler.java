package cn.enjoyedu.ch01.aio.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 *  网络写的处理器，CompletionHandler<Integer,ByteBuffer>中：
 *  Integer: 本次网络写操作完成实际写入的字节数
 *  ByteBuffer： 写操作的附件，存储了写操作需要写入的数据
 */
public class AioClientWriteHandler
        implements CompletionHandler<Integer,ByteBuffer> {
    private AsynchronousSocketChannel clientChannel;
    private CountDownLatch latch;
    public AioClientWriteHandler(AsynchronousSocketChannel clientChannel, CountDownLatch latch) {
        this.clientChannel = clientChannel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        //有可能无法一次性将数据写完，需要检查缓冲区是否还有数据需要继续进行网络写入
        if(buffer.hasRemaining()){
            clientChannel.write(buffer,buffer,this);
        }else{
            //写操作已经完成，为读取服务端传回的数据建立缓冲区
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            clientChannel.read(readBuffer,readBuffer,new AioClientReadHander(clientChannel,latch));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.err.println("数据发送失败");
        try {
            clientChannel.close();
            latch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
