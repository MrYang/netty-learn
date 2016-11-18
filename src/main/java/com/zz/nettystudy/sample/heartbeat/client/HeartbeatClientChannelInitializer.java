package com.zz.nettystudy.sample.heartbeat.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

class HeartbeatClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private Logger logger = LoggerFactory.getLogger(HeartbeatClientChannelInitializer.class);

    private boolean addIdleStateHandler;

    private static final int READ_IDLE_TIME_OUT = 15; // 读超时
    private static final int WRITE_IDLE_TIME_OUT = 15;// 写超时
    private static final int ALL_IDLE_TIME_OUT = 20; // 所有超时

    HeartbeatClientChannelInitializer(boolean addIdleStateHandler) {
        this.addIdleStateHandler = addIdleStateHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());

        if (addIdleStateHandler) {
            pipeline.addLast(new IdleStateHandler(READ_IDLE_TIME_OUT,
                    WRITE_IDLE_TIME_OUT, ALL_IDLE_TIME_OUT, TimeUnit.SECONDS));
        } else {
            // 3分钟后发一个消息,由于1分钟服务器就会关掉认为掉线的连接,channel.close之后客户端会自动关闭
            ch.eventLoop().schedule(() -> {
                logger.info("client: 迟到3分钟的消息");
                ch.writeAndFlush("迟到3分钟的消息\r\n");
            }, 3, TimeUnit.MINUTES);
        }

        pipeline.addLast(new HeartbeatClientHandler(ch));
    }
}