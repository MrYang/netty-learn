package com.zz.nettystudy.push.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class MonitorServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private AppContext context;

    private Logger logger = LoggerFactory.getLogger(MonitorServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;

            String url = request.getUri();
            logger.info("request url:{}", url);

            if (url.equals("/favicon.ico")) {
                return;
            }
            String responseJson = "";

            // 获取请求参数
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(url);
            String path = queryStringDecoder.path();
            switch (path) {
                case "/queue":
                    responseJson = "{\"size\":" + context.statQueue() + "}";
                    break;
                case "/online":
                    break;
                case "/push":
                    break;
            }

            // 响应JSON
            byte[] responseBytes = responseJson.getBytes("UTF-8");
            int contentLength = responseBytes.length;

            // 构造FullHttpResponse对象，FullHttpResponse包含message body
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseBytes));
            response.headers().set("Content-Type", "application/json;charset=UTF-8");
            response.headers().set("Content-Length", Integer.toString(contentLength));

            ctx.writeAndFlush(response);
        }
    }

}
