package com.zz.nettystudy.push.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

    private String id;  // 消息id

    private String deviceId;

    private String title;

    private String content;

    private LocalDateTime createTime;
    private LocalDateTime pushTime;

    private int type;   // 系统消息,群消息,点对点消息|文字消息,图片消息,语音消息|在线,离线
    private int subtype;    // 上线,下线,

    private boolean receipt;    // 是否需要回执

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubtype() {
        return subtype;
    }

    public void setSubtype(int subtype) {
        this.subtype = subtype;
    }

    public boolean isReceipt() {
        return receipt;
    }

    public void setReceipt(boolean receipt) {
        this.receipt = receipt;
    }
}
