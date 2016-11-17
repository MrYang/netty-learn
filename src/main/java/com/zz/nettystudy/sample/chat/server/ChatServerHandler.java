package com.zz.nettystudy.sample.chat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private Logger logger = LoggerFactory.getLogger(ChatServer.class);

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();

        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\r\n");
        channels.add(incoming);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();

        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\r\n");
        channels.remove(incoming);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            if (channel != incoming) {
                channel.writeAndFlush("[" + incoming.remoteAddress() + "] say: " + msg + "\r\n");
            } else {
                channel.writeAndFlush("[you] say: " + msg + "\n");
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        logger.info("chatClient:{}在线", incoming.remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        channels.remove(incoming);
        logger.info("chatClient:{}掉线", incoming.remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        logger.info("chatClient:{}异常", incoming.remoteAddress());
        channels.remove(incoming);
        cause.printStackTrace();
        ctx.close();
    }
}
