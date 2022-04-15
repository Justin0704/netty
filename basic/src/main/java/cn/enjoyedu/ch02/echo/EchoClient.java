package cn.enjoyedu.ch02.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {

    private final int port;
    private final String host;

    public EchoClient(int port, String host){
        this.port = port;
        this.host = host;
    }

    public static void main(String[] args) throws  Exception{
        EchoClient client = new EchoClient(9999,"127.0.0.1");
        client.start();
    }

    public void start() throws Exception {
        //线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            //客户端启动必须
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)//使用nio进行网络通讯
                    .remoteAddress(new InetSocketAddress(host, port))//配置远程服务器地址
                    .handler(new EchoClientHandler());
            //连接到远程节点，阻塞等待直到链接完成
            ChannelFuture channelFuture = bootstrap.connect().sync();
            //阻塞，直到channel关闭
            channelFuture.channel().closeFuture().sync();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            group.shutdownGracefully().sync();
        }
    }
}
