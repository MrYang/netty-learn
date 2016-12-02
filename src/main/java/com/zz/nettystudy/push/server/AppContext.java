package com.zz.nettystudy.push.server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.zz.nettystudy.push.common.Constants;
import com.zz.nettystudy.push.common.entity.Device;
import com.zz.nettystudy.push.common.entity.ServerMessage;
import com.zz.nettystudy.push.server.repository.MessageRepository;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class AppContext {

    private static Logger logger = LoggerFactory.getLogger(AppContext.class);

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // device 与 channel 对应map
    private static BiMap<String, Channel> deviceChannelMap = HashBiMap.create();

    // 在线设备
    public static Map<String, Device> onlineDevice = new HashMap<>();

    public static MessageRepository messageRepository = SpringContext.getBean(MessageRepository.class);

    // 可以根据消息id把消息放在不同的队列,由多个线程去推送
    public static BlockingQueue<ServerMessage> queue = new LinkedBlockingDeque<>();

    public static void online(String deviceId, Channel channel) {

        logger.info("{}设备上线了", deviceId);

        channels.add(channel);
        deviceChannelMap.put(deviceId, channel);

        Device device = new Device();
        device.setId(deviceId);
        device.setLastHeartTime(LocalDateTime.now());
        onlineDevice.put(deviceId, device);

        // 搜索该设备的离线消息,并加入到队列发送
        List<ServerMessage> msg = messageRepository.findByDeviceId(deviceId);
        msg.forEach(AppContext::addMessage2Queue);
    }

    public static void offline(String deviceId, Channel channel) {
        logger.info("{}设备下线了, 主动下线", deviceId);

        channels.remove(channel);
        deviceChannelMap.remove(deviceId, channel);

        onlineDevice.remove(deviceId);
    }

    public static void offline(Channel channel) {
        channels.remove(channel);
        BiMap<Channel, String> biMap = deviceChannelMap.inverse();
        String deviceId = biMap.get(channel);

        logger.info("{}设备下线了", deviceId);

        deviceChannelMap.remove(deviceId); // 反转后的map修改操作会影响原来的map
        onlineDevice.remove(deviceId);
    }

    public static void offline(String deviceId) {
        logger.info("{}设备下线了,长时间没有心跳", deviceId);
        deviceChannelMap.remove(deviceId);
        channels.remove(deviceChannelMap.get(deviceId));
        onlineDevice.remove(deviceId);
    }

    public static void ping(String deviceId) {
        logger.info("{} 发送ping消息", deviceId);
        Device device = onlineDevice.get(deviceId);
        device.setLastHeartTime(LocalDateTime.now());
        onlineDevice.put(deviceId, device);
    }

    public static void addMessage2Queue(ServerMessage msg) {
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void pushMessage(ServerMessage msg) {
        String deviceId = msg.getDeviceId();
        Channel channel = deviceChannelMap.get(deviceId);
        if (channel != null) {
            if (isOnline(deviceId)) {
                msg.setPushTime(LocalDateTime.now());
                logger.info("推送消息给{}", deviceId);
                channel.writeAndFlush(msg);

                if (msg.getOnline() == Constants.SERVER_MESSAGE_OFFLINE) {
                    messageRepository.delete(msg.getId());
                }
            } else {
                msg.setOnline(Constants.SERVER_MESSAGE_OFFLINE);
                messageRepository.save(msg);
                logger.info("保存离线消息:{}给{}", msg, deviceId);
            }
        } else {
            msg.setOnline(Constants.SERVER_MESSAGE_OFFLINE);
            messageRepository.save(msg);
            logger.info("保存离线消息:{}给{}", msg, deviceId);
        }
    }

    public static int statQueue() {
        return queue.size();
    }

    public static int onlineSize() {
        return onlineDevice.size();
    }

    public static boolean isOnline(String deviceId) {
        return onlineDevice.containsKey(deviceId);
    }
}
