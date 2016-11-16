package com.zz.nettystudy.sample.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private Logger logger = LoggerFactory.getLogger(EchoClientHandler.class);

    /**
     * 此方法会在连接到服务器后被调用
     */
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("client: channelActive event");

        ctx.writeAndFlush(Unpooled.copiedBuffer("connect server", CharsetUtil.UTF_8));
    }

    /**
     * 此方法会在接收到服务器数据后调用
     */
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        logger.info("client: channelRead0 event");

        logger.info("client: received: " + in.toString(CharsetUtil.UTF_8));
    }

    /**
     * 捕捉到异常
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.info("client: exceptionCaught event");

        cause.printStackTrace();
        ctx.close();
    }
}
