package cn.enjoyedu.ch02.splicing.fixed;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

import java.net.InetSocketAddress;

public class FixedLengthEchoServer {

    public static final String RESPONSE = "Welcome to Netty";
    private final int port;

    public FixedLengthEchoServer(int port){
        this.port = port;
    }


    private void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializerImpl());
            ChannelFuture channelFuture = b.bind().sync();
            System.out.println("服务器启动完成，等待客户端的连接和数据...");
            channelFuture.channel().closeFuture().sync();
        }finally {
            //优雅的关闭线程组
            group.shutdownGracefully().sync();
        }
    }

    private static class ChannelInitializerImpl extends io.netty.channel.ChannelInitializer<Channel>{
        @Override
        protected void initChannel(Channel channel) throws Exception {
            channel.pipeline().addLast(new FixedLengthFrameDecoder(FixedLengthEchoClient.REQUEST.length()));
            channel.pipeline().addLast(new FixedLengthEchoServerHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new FixedLengthEchoServer(8888).start();
    }
}
