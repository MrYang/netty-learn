package com.zz.nettystudy.sample.chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient {

    public static void main(String[] args) throws Exception {

        String host = "127.0.0.1";
        int port = 5000;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChatClientChannelInitializer());

            ChannelFuture f = b.connect(host, port).sync();

            Channel channel = f.channel();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String msg = in.readLine();
                if ("bye".equals(msg) || msg == null) {
                    break;
                }

                channel.writeAndFlush(msg + "\r\n");
            }
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
