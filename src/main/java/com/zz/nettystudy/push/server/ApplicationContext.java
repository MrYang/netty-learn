package com.zz.nettystudy.push.server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.zz.nettystudy.push.common.entity.Message;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class ApplicationContext {

    private Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static BiMap<String, Channel> deviceChannelMap = HashBiMap.create();

    public static BlockingQueue<Message> queue = new LinkedBlockingDeque<>();

    public void addChannel(String deviceId, Channel channel) {
        channels.add(channel);
        deviceChannelMap.put(deviceId, channel);
    }

    public void removeChannel(String deviceId, Channel channel) {
        channels.remove(channel);
        deviceChannelMap.remove(deviceId, channel);
    }

    public void addMessage2Queue(Message msg) {
        queue.add(msg);
    }

    public void pushMessage(Message msg) {
        String deviceId = msg.getDeviceId();
        Channel channel = deviceChannelMap.get(deviceId);
        if (channel != null) {
            msg.setPushTime(LocalDateTime.now());
            // save message
            channel.writeAndFlush(msg);
        }
    }
}
