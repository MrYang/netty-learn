package com.zz.nettystudy.sample.codec.client;

import com.zz.nettystudy.common.entity.Request;
import com.zz.nettystudy.sample.codec.CodecDecoder;
import com.zz.nettystudy.sample.codec.CodecEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class CodecClient {

    private static Logger logger = LoggerFactory.getLogger(CodecClient.class);

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 5000;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new CodecDecoder());
                    ch.pipeline().addLast(new CodecEncoder());
                    ch.pipeline().addLast(new CodecClientHandler());
                }
            });

            ChannelFuture f = b.connect(host, port).sync();
            Request request = new Request();
            request.setId("1");
            request.setType(1);
            request.setBody("request body");
            request.setHeaders(new HashMap<>());
            f.channel().writeAndFlush(request).sync();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
