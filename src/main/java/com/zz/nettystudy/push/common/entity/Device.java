package com.zz.nettystudy.push.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table
@Entity
public class Device {

    @Id
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
