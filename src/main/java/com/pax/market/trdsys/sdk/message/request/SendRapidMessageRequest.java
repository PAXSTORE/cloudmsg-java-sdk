package com.pax.market.trdsys.sdk.message.request;

import org.json.JSONObject;

import java.io.Serializable;

public class SendRapidMessageRequest implements Serializable {
    private String serialNo;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(JSONObject data) {
        this.content = data==null?null:data.toString();
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

}
