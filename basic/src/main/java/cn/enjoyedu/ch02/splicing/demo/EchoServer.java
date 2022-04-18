package cn.enjoyedu.ch02.splicing.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

    private final int port;

    public EchoServer(int port){
        this.port = port;
    }
    public void start() throws InterruptedException {
        //定义一个线程组
        EventLoopGroup group = new NioEventLoopGroup();
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        try {
            //服务器端启动
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)//指定nio进行网络通讯
                    .localAddress(new InetSocketAddress(port))//指定服务器监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //接收到链接请求，新起一个socket通信，也就是channel，每个channel有自己的handler事件
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(echoServerHandler);
                        }
                    });
            // 绑定到端口，阻塞等待，直到链接完成
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println("服务器启动完成，等待客户端的连接和数据...");
            //阻塞，直到channel关闭
            channelFuture.channel().closeFuture().sync();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            //优雅的关闭线程组
            group.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("服务器即将启动...");
        new EchoServer(9999).start();
        System.out.println("服务器关闭");
    }
}
