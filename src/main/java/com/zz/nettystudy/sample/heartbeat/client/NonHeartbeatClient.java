package com.zz.nettystudy.sample.heartbeat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NonHeartbeatClient {

    private static Logger logger = LoggerFactory.getLogger(NonHeartbeatClient.class);

    public static void main(String[] args) throws Exception {

        String host = "127.0.0.1";
        int port = 5000;

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new HeartbeatClientChannelInitializer(false));

            ChannelFuture f = b.connect(host, port).sync();
            f.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        logger.info("client connected success");
                    } else {
                        logger.info("client connected failed");
                        future.cause().printStackTrace();
                    }
                }
            });
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
