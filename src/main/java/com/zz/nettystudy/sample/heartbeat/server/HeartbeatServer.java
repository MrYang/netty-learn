package com.zz.nettystudy.sample.heartbeat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartbeatServer {

    private Logger logger = LoggerFactory.getLogger(HeartbeatServer.class);

    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

    private int port;

    private HeartbeatServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new HeartbeatServer(5000).run();
    }

    private void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HeartbeatServerChannelInitializer());

            ChannelFuture f = b.bind(port).sync();
            logger.info("start server on port:{}", port);
            //pool.scheduleAtFixedRate(SessionManager.getInstance().pushRunnable, 1, 25, TimeUnit.SECONDS);
            pool.scheduleAtFixedRate(SessionManager.getInstance().removeOfflineRunnable, 1, 60, TimeUnit.SECONDS);

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
