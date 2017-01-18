package com.zz.nettystudy.sample.codec.server;

import com.zz.nettystudy.common.entity.Request;
import com.zz.nettystudy.common.entity.Response;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CodecServerHandler extends SimpleChannelInboundHandler<Request> {

    private Logger logger = LoggerFactory.getLogger(CodecServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request msg) throws Exception {
        logger.info("server: request:{}", msg);

        Response response = new Response();
        response.setId(msg.getId());
        response.setStatusCode(200);
        response.setResult("response body");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
