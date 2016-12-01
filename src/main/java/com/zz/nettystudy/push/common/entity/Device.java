package com.zz.nettystudy.push.common.entity;

import java.time.LocalDateTime;

public class Device {

    private String id;  // mac
    private String appId;
    private LocalDateTime lastHeartTime;    // 上次心跳时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public LocalDateTime getLastHeartTime() {
        return lastHeartTime;
    }

    public void setLastHeartTime(LocalDateTime lastHeartTime) {
        this.lastHeartTime = lastHeartTime;
    }
}
