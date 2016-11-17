package com.zz.nettystudy.sample.chat.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    private Logger logger = LoggerFactory.getLogger(ChatClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.info("client: receive msg:{}", msg);
    }
}
