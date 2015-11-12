package com.zz.nettystudy.server.time;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TimeServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new TimeServerHandler());
    }
}
