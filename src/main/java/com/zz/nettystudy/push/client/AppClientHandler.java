package com.zz.nettystudy.push.client;

import com.zz.nettystudy.push.common.entity.ServerMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AppClientHandler extends SimpleChannelInboundHandler<ServerMessage> {

    private Logger logger = LoggerFactory.getLogger(AppClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerMessage msg) throws Exception {
        logger.info("receive push message:{}", msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
