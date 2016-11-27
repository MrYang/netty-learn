package com.zz.nettystudy.push.server;

import com.zz.nettystudy.sample.codec.CodecDecoder;
import com.zz.nettystudy.sample.codec.CodecEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class PushServer {

    private Logger logger = LoggerFactory.getLogger(PushServer.class);

    @Value("${server.port:5000}")
    private int port;

    @Autowired
    private PushServerHandler pushServerHandler;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new CodecDecoder());
                        ch.pipeline().addLast(new CodecEncoder());
                        ch.pipeline().addLast(pushServerHandler);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture f = b.bind(port).sync();
        logger.info("push server start, listen:{}", port);
        f.channel().closeFuture().sync();
    }

    @PreDestroy
    public void close() {
        logger.info("push server shutdown");
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
