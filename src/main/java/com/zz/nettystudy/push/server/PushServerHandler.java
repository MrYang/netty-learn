package com.zz.nettystudy.push.server;

import com.zz.nettystudy.push.common.entity.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PushServerHandler extends SimpleChannelInboundHandler<Message> {

    @Autowired
    private ApplicationContext context;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        int subtype = msg.getSubtype();
        String deviceId = msg.getDeviceId();
        switch (subtype) {
            case Constants.ON:
                context.addChannel(deviceId, ctx.channel());
                break;
            case Constants.OFF:
                context.removeChannel(deviceId, ctx.channel());
                break;
            default:
                throw new IllegalArgumentException("不支持该消息类型");
        }
    }


    public void channelReadComplete(ChannelHandlerContext ctx) {
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }
}
