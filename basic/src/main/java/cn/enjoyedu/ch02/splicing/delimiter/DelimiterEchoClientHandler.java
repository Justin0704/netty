package cn.enjoyedu.ch02.splicing.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class DelimiterEchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private AtomicInteger counter = new AtomicInteger(0);

    /**
     * 客户端被通知channel活跃后，dosomething
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf;
        String request = "Hello, i am leaning netty, it's hard to programming" + System.getProperty("line.separator");
        for(int i = 0; i < 10; i++){
            Thread.sleep(500);
            System.out.println("即将发送数据：" + request);
            byteBuf = Unpooled.buffer(request.length());
            byteBuf.writeBytes(request.getBytes());
            ctx.writeAndFlush(byteBuf);
        }
    }

    /**
     * 客户端读取到网络数据后处理
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("Client accept[" + byteBuf.toString(CharsetUtil.UTF_8) + "] and the counter is " + counter.incrementAndGet());
        channelHandlerContext.close();
    }

    /**
     * 发生异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
