package com.zz.nettystudy.push.server;

import com.zz.nettystudy.push.common.entity.ClientMessage;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientMessageProcessor {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    private Channel channel;
    private ClientMessage message;

    public ClientMessageProcessor(Channel channel, ClientMessage message) {
        this.channel = channel;
        this.message = message;
    }

    public void process() {
        executorService.execute(() -> {
            switch (message.getType()) {
                case Constants.MESSAGE_TYPE_ON:
                    AppContext.online(message.getDeviceId(), channel);
                    break;
                case Constants.MESSAGE_TYPE_OFF:
                    AppContext.offline(message.getDeviceId(), channel);
                    break;
                case Constants.MESSAGE_TYPE_PING:
                    AppContext.ping(message.getDeviceId());
                    break;
                case Constants.MESSAGE_TYPE_RECEIPT:
                    break;
                default:
                    throw new IllegalArgumentException("不能处理该消息");
            }
        });
    }
}
