package com.zz.nettystudy.push.common.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class ClientMessage implements Serializable {
    private String deviceId;
    private int type;   // 客户端发的消息:上线,下线,ping,receipt
    private String content; // 客户端发送给服务端的消息,使用json格式,比较灵活

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
