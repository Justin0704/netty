package cn.enjoyedu.ch02.splicing.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private AtomicInteger counter = new AtomicInteger(0);

    /**
     * 客户端读取网络数据后的处理
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("客户端的channelRead0方法");
        System.out.println("Client accept[" + byteBuf.toString(CharsetUtil.UTF_8) + "] and the counter is " + counter.incrementAndGet());
    }

    /**
     * 客户端被通知active活跃后，做的事情
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端的channelActive方法");
        ByteBuf byteBuf;
        String request = "Hello, i am leaning netty, it's hard to programming" + System.getProperty("line.separator");
        for(int i = 0; i < 100;i ++){
            byteBuf = Unpooled.buffer(request.length());
            byteBuf.writeBytes(request.getBytes());
            ctx.writeAndFlush(byteBuf);
        }

    }

    /**
     * 发出异常后的处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端的exceptionCaught方法");
        cause.printStackTrace();
        ctx.close();
    }
}
