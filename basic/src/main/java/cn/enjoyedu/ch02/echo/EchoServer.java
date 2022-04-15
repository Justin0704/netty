package cn.enjoyedu.ch02.echo;

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

    public static void main(String[] args) throws Exception {
        EchoServer server = new EchoServer(9999);
        server.start();
    }

    public void start() throws Exception {
        /** 定义一个线程组 */
        EventLoopGroup group = new NioEventLoopGroup();
        final EchoServerHandler serverHandler = new EchoServerHandler();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();//服务器端启动
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)//指定使用nio进行网络通讯
                    .localAddress(new InetSocketAddress(port))//指定服务器监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /** 接收到链接请求，新起一个socket通信，也就是channel，每个channel有自己的handler事件 */
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });
            // 绑定到端口，阻塞等待，直到链接完成
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            //阻塞，直到channel关闭
            channelFuture.channel().closeFuture().sync();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            group.shutdownGracefully().sync();
        }

    }
}
