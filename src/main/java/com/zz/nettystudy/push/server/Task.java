package com.zz.nettystudy.push.server;

import com.zz.nettystudy.push.common.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Task {

    private Logger logger = LoggerFactory.getLogger(Task.class);

    @Autowired
    private ApplicationContext applicationContext;

    private boolean stopPushMessage = false;

    public void kickOffline() {
        logger.info("start kick offline task");

    }

    public void heartbeat() {
        logger.info("start heartbeat task");
    }

    @PostConstruct
    public void pushMessage() {
        logger.info("push message");
        while (!stopPushMessage) {
            try {
                Message message = ApplicationContext.queue.take();
                while (message != null) {
                    applicationContext.pushMessage(message);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    public void close() {
        stopPushMessage = true;
    }
}
