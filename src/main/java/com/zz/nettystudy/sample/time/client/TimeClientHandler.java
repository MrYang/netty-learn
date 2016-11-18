package com.zz.nettystudy.sample.time.client;

import com.zz.nettystudy.common.entity.UnixTime;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TimeClientHandler extends SimpleChannelInboundHandler<UnixTime> {

    private Logger logger = LoggerFactory.getLogger(TimeClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UnixTime msg) throws Exception {
        logger.info("receive time:{}", msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
