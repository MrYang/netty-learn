package com.zz.nettystudy.server.discard;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class DiscardServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new DiscardServerHandler());
    }
}
