package com.pax.market.trdsys.sdk.message.request;

import com.pax.market.trdsys.sdk.message.MsgType;
import com.pax.market.trdsys.sdk.message.dto.MsgContent;

import java.io.Serializable;

public class SingleTerminalMsgCreateReqeust implements Serializable {

    private static final long serialVersionUID = 1L;

    private int msgType;

    private String serialNo;

    private String tid;

    private MsgContent content;

    private boolean sendByTid = false;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType.val();
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public MsgContent getContent() {
        return content;
    }

    public void setContent(MsgContent content) {
        this.content = content;
    }

    public boolean isSendByTid() {
        return sendByTid;
    }

    public void setSendByTid(boolean sendByTid) {
        this.sendByTid = sendByTid;
    }
}
