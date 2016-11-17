package com.zz.nettystudy.sample.heartbeat.server;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private Logger logger = LoggerFactory.getLogger(SessionManager.class);

    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Map<Channel, LocalDateTime> onlineChannel = new ConcurrentHashMap<>(16);

    private static volatile SessionManager sessionManager;

    // 定时删除1分钟没有读写的channel, 可以做成队列或者其他的数据结构
    public Runnable removeOfflineRunnable = () -> {
        logger.info("run remove offline task");
        Iterator<Channel> iterator = onlineChannel.keySet().iterator();
        while (iterator.hasNext()) {
            Channel channel = iterator.next();
            LocalDateTime dateTime = onlineChannel.get(channel);
            if (dateTime.plusMinutes(1).isBefore(LocalDateTime.now())) {
                logger.info("remove channel:{}", channel.remoteAddress());
                channelGroup.remove(channel);
                iterator.remove();
            }
        }
    };

    public Runnable pushRunnable = () -> {
        logger.info("run push message task");
        channelGroup.forEach(channel -> {
            logger.info("push to:{}", channel.remoteAddress());
            channel.writeAndFlush("schedule push msg\r\n");
        });
    };

    public static SessionManager getInstance() {
        if (sessionManager == null) {
            synchronized (SessionManager.class) {
                if (sessionManager == null) {
                    sessionManager = new SessionManager();
                }
            }
        }
        return sessionManager;
    }

    public void add(Channel channel) {
        channelGroup.add(channel);
        onlineChannel.put(channel, LocalDateTime.now());
    }

    public void remove(Channel channel) {
        channelGroup.remove(channel);
        onlineChannel.remove(channel);
    }

    public void touch(Channel channel) {
        onlineChannel.put(channel, LocalDateTime.now());
    }
}
