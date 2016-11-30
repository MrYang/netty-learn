package com.zz.nettystudy.push.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.UnsupportedEncodingException;

public class RouterProcessor {

    private Channel channel;

    private String path;

    public RouterProcessor(Channel channel, String path) {
        this.channel = channel;
        this.path = path;
    }

    public void process() throws UnsupportedEncodingException {
        String json = "";
        byte[] responseBytes = json.getBytes("UTF-8");
        int contentLength = responseBytes.length;

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseBytes));
        response.headers().set("Content-Type", "application/json;charset=UTF-8");
        response.headers().set("Content-Length", Integer.toString(contentLength));

        channel.writeAndFlush(response);
    }
}
