package cn.enjoyedu.ch02.splicing.fixed;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class FixedLengthEchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{

    private AtomicInteger counter = new AtomicInteger(0);

    /**
     * 客户端被通知活跃后的处理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = null;
        for(int i=0; i<10; i++){
            //取得发送数据的大小
            byteBuf = Unpooled.buffer(FixedLengthEchoClient.REQUEST.length());
            //向buffer中写入数据
            byteBuf.writeBytes(FixedLengthEchoClient.REQUEST.getBytes());
            //通过channel发送数据
            ctx.writeAndFlush(byteBuf);
        }
    }

    /**
     * 客户端读取到网络后的处理
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("Client Accept [" + byteBuf.toString(CharsetUtil.UTF_8) + "] and the counter is " + counter.incrementAndGet());
    }

    /**
     * 发生异常后的处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
