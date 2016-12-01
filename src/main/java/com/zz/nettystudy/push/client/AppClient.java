package com.zz.nettystudy.push.client;

import com.zz.nettystudy.push.common.Constants;
import com.zz.nettystudy.push.common.entity.ClientMessage;
import com.zz.nettystudy.sample.codec.CodecDecoder;
import com.zz.nettystudy.sample.codec.CodecEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppClient {

    private static Logger logger = LoggerFactory.getLogger(AppClient.class);

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private int port = 5000;
    private String host = "127.0.0.1";
    public static Channel channel;

    public void start() throws InterruptedException {
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
                    ch.pipeline().addLast(new AppClientHandler());
                }
            });

            ChannelFuture f = b.connect(host, port).sync();
            f.addListener((ChannelFutureListener) future -> {
                logger.info("connect to server: {}:{}", host, port);

                // 发送上线消息
                channel = f.channel();
                ClientMessage onMsg = new ClientMessage();
                onMsg.setType(Constants.MESSAGE_TYPE_ON);
                onMsg.setDeviceId(Constants.CLINET_DEVICE_ID);
                channel.writeAndFlush(onMsg);

                new Task().heartbeat();
            });

            f.channel().closeFuture().addListener((ChannelFutureListener) future -> {
                logger.info("disconnect to server: {}:{}", host, port);

                // 发送下线消息
                ClientMessage offMsg = new ClientMessage();
                offMsg.setType(Constants.MESSAGE_TYPE_OFF);
                offMsg.setDeviceId(Constants.CLINET_DEVICE_ID);
                channel.writeAndFlush(offMsg);
            }).sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new AppClient().start();
    }
}
