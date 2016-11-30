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
                    on(channel, message);
                    break;
                case Constants.MESSAGE_TYPE_OFF:
                    break;
                case Constants.MESSAGE_TYPE_PING:
                    break;
                case Constants.MESSAGE_TYPE_RECEIPT:
                    break;
            }
        });
    }

    private void on(Channel channel, ClientMessage message) {

    }

    private void off(Channel channel, ClientMessage message) {

    }

    private void ping(Channel channel, ClientMessage message) {

    }
}
