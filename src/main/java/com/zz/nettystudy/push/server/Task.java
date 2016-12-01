package com.zz.nettystudy.push.server;

import com.zz.nettystudy.push.common.entity.Device;
import com.zz.nettystudy.push.common.entity.ServerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Iterator;

@Component
public class Task {

    private Logger logger = LoggerFactory.getLogger(Task.class);

    private boolean stopPushMessage = false;

    /**
     * 设置了IdleStateHandler 之后,好像都不需要定时清除下线列表了
     */
    //@Scheduled(initialDelay = 1000, fixedDelay = 30000)
    public void kickOffline() {
        Iterator<String> iterator = AppContext.onlineDevice.keySet().iterator();
        while (iterator.hasNext()) {
            String deviceId = iterator.next();
            Device device = AppContext.onlineDevice.get(deviceId);
            if (device.getLastHeartTime().plusMinutes(3).isBefore(LocalDateTime.now())) {
                AppContext.offline(deviceId);
                iterator.remove();
            }
        }
    }

    @PostConstruct
    public void pushMessage() {
        while (!stopPushMessage) {
            try {
                ServerMessage message = AppContext.queue.take();
                AppContext.pushMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        stopPushMessage = true;
    }
}
