package com.zz.nettystudy.push.server;

import com.zz.nettystudy.push.common.entity.ClientMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class PushServerHandler extends SimpleChannelInboundHandler<ClientMessage> {

    private Logger logger = LoggerFactory.getLogger(PushServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientMessage msg) throws Exception {
        logger.info("client msg:{}", msg);
        Channel channel = ctx.channel();
        new ClientMessageProcessor(channel, msg).process();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                logger.info("reader idle");
                AppContext.offline(ctx.channel());
                ctx.channel().disconnect();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    public void channelReadComplete(ChannelHandlerContext ctx) {
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.info("exceptionCaught");
        AppContext.offline(ctx.channel());
        ctx.channel().disconnect();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel inactive");
        AppContext.offline(ctx.channel());
        ctx.channel().disconnect();
    }
}
