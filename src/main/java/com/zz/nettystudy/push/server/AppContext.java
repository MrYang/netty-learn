package com.zz.nettystudy.push.server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.zz.nettystudy.push.common.entity.ServerMessage;
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
public class AppContext {

    private Logger logger = LoggerFactory.getLogger(AppContext.class);

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static BiMap<String, Channel> deviceChannelMap = HashBiMap.create();

    public static BlockingQueue<ServerMessage> queue = new LinkedBlockingDeque<>();

    public void addChannel(String deviceId, Channel channel) {
        channels.add(channel);
        deviceChannelMap.put(deviceId, channel);
    }

    public void removeChannel(String deviceId, Channel channel) {
        channels.remove(channel);
        deviceChannelMap.remove(deviceId, channel);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
        deviceChannelMap.inverse().remove(channel); // 反转后的map修改操作会影响原来的map
    }

    public void addMessage2Queue(ServerMessage msg) {
        queue.add(msg);
    }

    public void pushMessage(ServerMessage msg) {
        String deviceId = msg.getDeviceId();
        Channel channel = deviceChannelMap.get(deviceId);
        if (channel != null) {
            msg.setPushTime(LocalDateTime.now());
            // save message
            channel.writeAndFlush(msg);
        }
    }

    public int statQueue() {
        return queue.size();
    }
}
