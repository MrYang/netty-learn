package com.zz.nettystudy.sample.http.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

class HttpServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // server端发送的是httpResponse，使用HttpResponseEncoder进行编码
        ch.pipeline().addLast(new HttpResponseEncoder());
        // server端接收到的是httpRequest，使用HttpRequestDecoder进行解码
        ch.pipeline().addLast(new HttpRequestDecoder());

        pipeline.addLast(new HttpServerHandler());
    }
}