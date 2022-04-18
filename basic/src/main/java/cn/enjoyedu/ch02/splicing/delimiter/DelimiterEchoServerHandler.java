package cn.enjoyedu.ch02.splicing.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

@ChannelHandler.Sharable
public class DelimiterEchoServerHandler extends ChannelInboundHandlerAdapter {

    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务器端 - channelActive");
        System.out.println("客户端：【" + ctx.channel().remoteAddress() + "]已连接。。。。。。");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器端 - channelRead");
        ByteBuf byteBuf = (ByteBuf) msg;
        String request = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("Server Accept [" + request + "] and counter is: " + counter.incrementAndGet());
        //服务器的应答也要添加一个回车换行符
        String resp = "Hello, " + request + ". Welcome to netty world!" + DelimiterEchoServer.DELIMITER_SYMBOL;
        ctx.writeAndFlush(Unpooled.copiedBuffer(resp.getBytes()));
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务器端 - channelReadComplete");
    }

    /**
     * 服务器端发生异常后的处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("服务器端 - exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }
}
