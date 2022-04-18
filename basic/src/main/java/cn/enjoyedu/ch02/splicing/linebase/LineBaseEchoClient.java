package cn.enjoyedu.ch02.splicing.linebase;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

public class LineBaseEchoClient {


    private final int port;
    private final String host;

    public LineBaseEchoClient(int port, String host){
        this.port = port;
        this.host = host;
    }

    private void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)//将线程组传入
                    .channel(NioSocketChannel.class)//指定NIO进行网络传输
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializerImpl());
            ChannelFuture channelFuture = bootstrap.connect().sync();
            System.out.println("已连接到服务器...");
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    private static class ChannelInitializerImpl extends ChannelInitializer<Channel> {
        @Override
        protected void initChannel(Channel channel) throws Exception {
            //回车换行符做了个分割
            channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
            channel.pipeline().addLast(new LineBaseEchoClientHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new LineBaseEchoClient(9999, "127.0.0.1").start();
    }

}
