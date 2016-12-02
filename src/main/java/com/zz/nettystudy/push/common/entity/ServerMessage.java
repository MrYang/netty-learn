package com.zz.nettystudy.push.common.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "message")
@Entity
public class ServerMessage implements Serializable {

    @Id
    private String id;  // 消息id
    private String deviceId;
    private String title;
    private String content;

    private LocalDateTime createTime;
    private LocalDateTime pushTime;

    private boolean receipt;    // 是否需要回执

    private int online;   // 0,在线, 1,离线

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getPushTime() {
        return pushTime;
    }

    public void setPushTime(LocalDateTime pushTime) {
        this.pushTime = pushTime;
    }

    public boolean isReceipt() {
        return receipt;
    }

    public void setReceipt(boolean receipt) {
        this.receipt = receipt;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
