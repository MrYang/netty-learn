package com.zz.nettystudy.common.entity;

import java.io.Serializable;
import java.util.Map;

public class Request implements Serializable {

    private String id;

    private Map<String, String> headers;

    private int type;

    private String body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "id:" + id +
                ",type:" + type +
                ",heads:" + headers +
                ",body:" + body;
    }
}
