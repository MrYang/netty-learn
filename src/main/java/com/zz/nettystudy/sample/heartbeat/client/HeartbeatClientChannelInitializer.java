package com.zz.nettystudy.sample.heartbeat.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

class HeartbeatClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private boolean addIdleStateHandler;

    private static final int READ_IDLE_TIME_OUT = 15; // 读超时
    private static final int WRITE_IDLE_TIME_OUT = 15;// 写超时
    private static final int ALL_IDLE_TIME_OUT = 20; // 所有超时

    public HeartbeatClientChannelInitializer(boolean addIdleStateHandler) {
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
        }

        pipeline.addLast(new HeartbeatClientHandler(ch));
    }
}