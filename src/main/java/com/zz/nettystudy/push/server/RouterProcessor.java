package com.zz.nettystudy.push.server;

import com.zz.nettystudy.push.common.entity.ServerMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RouterProcessor {

    private Channel channel;
    private HttpRequest request;

    public RouterProcessor(Channel channel, HttpRequest request) {
        this.channel = channel;
        this.request = request;
    }

    public void process() throws UnsupportedEncodingException {
        String url = request.getUri();
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(url);
        String path = queryStringDecoder.path();
        String text = "";
        switch (path) {
            case "/queue":
                text = "msg queue size:" + AppContext.statQueue();
                break;
            case "/online":
                List<String> deviceIds = queryStringDecoder.parameters().get("deviceId");
                if (!deviceIds.isEmpty()) {
                    String deviceId = deviceIds.get(0);
                    text = deviceId + " isOnline: " + AppContext.isOnline(deviceId);
                }
                break;
            case "/push":
                deviceIds = queryStringDecoder.parameters().get("deviceId");
                if (!deviceIds.isEmpty()) {
                    String deviceId = deviceIds.get(0);
                    ServerMessage serverMessage = new ServerMessage();
                    serverMessage.setId(UUID.randomUUID().toString());
                    serverMessage.setDeviceId(deviceId);
                    serverMessage.setTitle("msg title");
                    serverMessage.setContent("msg content");
                    serverMessage.setCreateTime(LocalDateTime.now());
                    serverMessage.setReceipt(false);
                    AppContext.addMessage2Queue(serverMessage);
                    text = "push msg ok";
                }
                break;
            default:
                text = "ok";
        }

        _process(text);
    }

    private void _process(String text) throws UnsupportedEncodingException {
        byte[] responseBytes = text.getBytes("UTF-8");
        int contentLength = responseBytes.length;

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseBytes));
        response.headers().set("Content-Type", "text/plain;charset=UTF-8");
        response.headers().set("Content-Length", Integer.toString(contentLength));

        channel.writeAndFlush(response);
    }
}
