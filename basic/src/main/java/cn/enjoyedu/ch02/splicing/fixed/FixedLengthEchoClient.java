package cn.enjoyedu.ch02.splicing.fixed;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

import java.net.InetSocketAddress;

public class FixedLengthEchoClient {

    public static final String REQUEST = "Hello, i am leaning netty, it's hard to programming!";

    private final String host;
    private final int port;

    public FixedLengthEchoClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    private void start() throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelIniticalizeImpl());
            ChannelFuture channelFuture = b.connect().sync();
            System.out.println("已连接到服务器");
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    private static class ChannelIniticalizeImpl extends ChannelInitializer<Channel>{
        @Override
        protected void initChannel(Channel channel) throws Exception {
            channel.pipeline().addLast(new FixedLengthFrameDecoder(FixedLengthEchoServer.RESPONSE.length()));
            channel.pipeline().addLast(new FixedLengthEchoClientHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new FixedLengthEchoClient("127.0.0.1", 8888).start();
    }
}
