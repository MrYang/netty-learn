package com.zz.nettystudy.client.time;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TimeClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
    }
}
