package com.zz.nettystudy.sample.reconnect;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ReconnectClient {

    private static Logger logger = LoggerFactory.getLogger(ReconnectClient.class);

    private Bootstrap bootstrap;

    private EventLoopGroup workerGroup;

    private String host = "127.0.0.1";
    private int port = 5000;

    private void init() throws Exception {
        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addFirst(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        super.channelInactive(ctx);
                        logger.info("client inactive reconnect");
                        ctx.channel().eventLoop().schedule(() -> connect(), 10, TimeUnit.SECONDS);
                    }
                });
            }
        });

        connect();
    }

    private void connect() {
        ChannelFuture future = bootstrap.connect(host, port);

        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    logger.info("client connected success");
                } else {
                    logger.info("client connected failed");
                    f.channel().eventLoop().schedule(() -> connect(), 10, TimeUnit.SECONDS);
                }
            }
        });
    }

    private void close() {
        workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        new ReconnectClient().init();
    }
}
