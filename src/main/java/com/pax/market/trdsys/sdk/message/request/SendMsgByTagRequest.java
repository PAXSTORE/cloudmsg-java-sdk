package com.pax.market.trdsys.sdk.message.request;

import com.pax.market.trdsys.sdk.message.MsgType;
import com.pax.market.trdsys.sdk.message.dto.MsgContent;

import java.io.Serializable;

public class SendMsgByTagRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tagName;

    private int msgType;

    private MsgContent content;

    private int effectiveDays = 1;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType.val();
    }

    public MsgContent getContent() {
        return content;
    }

    public void setContent(MsgContent content) {
        this.content = content;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getEffectiveDays() {
        return effectiveDays;
    }

    public void setEffectiveDays(int effectiveDays) {
        this.effectiveDays = effectiveDays;
    }
}
