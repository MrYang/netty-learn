package com.zz.nettystudy.sample.codec.client;

import com.zz.nettystudy.common.entity.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CodecClientHandler extends SimpleChannelInboundHandler<Response> {

    private Logger logger = LoggerFactory.getLogger(CodecClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
        logger.info("client: response:{}", msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
