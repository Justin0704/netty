package cn.enjoyedu.ch02.splicing.linebase;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

public class LineBaseEchoServer {

    private final int port;

    public LineBaseEchoServer(int port){
        this.port = port;
    }

    private void start() throws InterruptedException {
        //定义一个线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            //服务器端启动
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)//将线程组传入
                    .channel(NioServerSocketChannel.class)//指定使用IO进行网络传输
                    .localAddress(new InetSocketAddress(port))//指定服务器监听端口
                    .childHandler(new ChannelInitializeImpl());//服务器端接收到一个连接请求，就会启动一个socket通信，也就是channel
            /**
             * 异步绑定到服务器，sync()会阻塞直到完成
             */
            ChannelFuture f = bootstrap.bind().sync();
            System.out.println("服务器启动完成，等待客户端的连接和数据...");
            /**
             * 阻塞知道服务器的channel关闭
             */
            f.channel().closeFuture().sync();
        }finally {
            //优雅的关闭线程组
            group.shutdownGracefully().sync();
        }
    }
    private static class ChannelInitializeImpl extends ChannelInitializer<Channel>{
        @Override
        protected void initChannel(Channel channel) throws Exception {
            /**
             * netty提供用来处理回车换行符，他把网络发来的报文按照回车换行符分解成一行行的数据
             * 然后再交给下一个handler处理
             */
            channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
            channel.pipeline().addLast(new LineBaseEchoServerHandler());
        }
    }
    public static void main(String[] args) throws InterruptedException {
        new LineBaseEchoServer(9999).start();
    }
}
