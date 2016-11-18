package com.zz.nettystudy.sample.chat.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        /**
         * decode 为入站事件(外部消息进来),处理顺序为顺序执行, 先进行解包,然后组装成字符串,最后到handler处理
         * 所以需要把framer排在decoder,handler 之前(framer->decode->handler)
         * encode 为出站事件(内部消息发出去)需要进行编码处理
         * 同理在client 端也需要配置framer,decoder,encoder这几个编解码器
         */
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("handler", new ChatServerHandler());
    }
}
