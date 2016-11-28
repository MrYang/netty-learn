package com.zz.nettystudy.push.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class MonitorServer {

    private Logger logger = LoggerFactory.getLogger(PushServer.class);

    @Value("${monitor.port:5001}")
    private int port;

    @Autowired
    private MonitorServerHandler monitorServerHandler;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, bossGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpResponseEncoder());
                        ch.pipeline().addLast(new HttpRequestDecoder());
                        ch.pipeline().addLast(monitorServerHandler);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture f = b.bind(port).sync();
        logger.info("monitor server start, listen:{}", port);
        //f.channel().closeFuture().sync();
    }

    @PreDestroy
    public void close() {
        logger.info("monitor server shutdown");
        bossGroup.shutdownGracefully();
    }
}
