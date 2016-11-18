package com.zz.nettystudy.common.entity;

import java.io.Serializable;

public class Response implements Serializable {

    private String id;

    private int statusCode;

    private String result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "id:" + id +
                ",statusCode:" + statusCode +
                ",result:" + result;
    }
}
