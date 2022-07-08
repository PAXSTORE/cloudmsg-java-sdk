/*
 * *******************************************************************************
 * COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or
 *   nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *   or disclosed except in accordance with the terms in that agreement.
 *
 *      Copyright (C) 2017 PAX Technology, Inc. All rights reserved.
 * *******************************************************************************
 */
package com.pax.market.trdsys.sdk.message.request;

import com.pax.market.trdsys.sdk.message.MsgType;
import com.pax.market.trdsys.sdk.message.dto.MsgContent;

import java.io.Serializable;

/**
 *
 * @author tanjie
 * @date  2019/04/24 14:05:50
 */
public class MessageCreateRequest implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 81643576327525513L;

	private int msgType;


	private String[] serialNos;

	private String[] tids;

	private MsgContent content;

	private boolean sendToSandboxTerminal = false;

	private boolean sendByTid = false;

	public MessageCreateRequest() {

	}

	public String[] getSerialNos() {
		return serialNos;
	}

	public void setSerialNos(String[] serialNos) {
		this.serialNos = serialNos;
	}

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

	public boolean isSendToSandboxTerminal() {
		return sendToSandboxTerminal;
	}

	public void setSendToSandboxTerminal(boolean sendToSandboxTerminal) {
		this.sendToSandboxTerminal = sendToSandboxTerminal;
	}

	public String[] getTids() {
		return tids;
	}

	public void setTids(String[] tids) {
		this.tids = tids;
	}

	public boolean isSendByTid() {
		return sendByTid;
	}

	public void setSendByTid(boolean sendByTid) {
		this.sendByTid = sendByTid;
	}
}
