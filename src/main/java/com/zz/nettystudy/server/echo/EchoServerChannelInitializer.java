package com.zz.nettystudy.server.echo;

import com.zz.nettystudy.server.discard.DiscardServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class EchoServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new EchoServerHandler());
    }
}
