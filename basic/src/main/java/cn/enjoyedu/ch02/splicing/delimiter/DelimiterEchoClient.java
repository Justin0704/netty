package cn.enjoyedu.ch02.splicing.delimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class DelimiterEchoClient {

    private final String host;
    private final int port;

    public DelimiterEchoClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    private void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)//将线程组传入
                    .channel(NioSocketChannel.class)//指定NIO进行网络传输
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializeImpl());
            ChannelFuture channelFuture = bootstrap.connect().sync();
            System.out.println("已连接到服务器...");
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    private static class ChannelInitializeImpl extends ChannelInitializer<Channel>{
        @Override
        protected void initChannel(Channel channel) throws Exception {
            channel.pipeline().addLast(new DelimiterEchoClientHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new DelimiterEchoClient("127.0.0.1", 9999).start();
    }
}
