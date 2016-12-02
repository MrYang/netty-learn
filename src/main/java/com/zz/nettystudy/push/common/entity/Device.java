package com.zz.nettystudy.push.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Device implements Serializable {

    private String id;  // mac
    private long appId;
    private LocalDateTime lastHeartTime;    // 上次心跳时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public LocalDateTime getLastHeartTime() {
        return lastHeartTime;
    }

    public void setLastHeartTime(LocalDateTime lastHeartTime) {
        this.lastHeartTime = lastHeartTime;
    }
}
