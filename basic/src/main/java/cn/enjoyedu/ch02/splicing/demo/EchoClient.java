package cn.enjoyedu.ch02.splicing.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 此例子会出现粘包 半包的问题
 */
public class EchoClient {

    private final int port;
    private final String host;

    public EchoClient(int port, String host){
        this.port = port;
        this.host = host;
    }

    public void start() throws InterruptedException {
        //定义一个线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            //客户端启动bootstrap
            final Bootstrap b = new Bootstrap();
            b.group(group)//传入线程组
                    .channel(NioSocketChannel.class)//指定NIO进行网络传输
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());//自定义handler
                        }
                    });
            ChannelFuture channelFuture = b.connect().sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient(9999, "127.0.0.1").start();
    }
}
