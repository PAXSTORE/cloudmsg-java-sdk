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
package com.pax.market.trdsys.sdk.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ResourceBundle;


/**
 *
 * @author tanjie
 * @date 2018-07-04
 */
public class MessageBoudleUtil {

	private static final String BASE_NAME = "ValidationMessages";
	private static final Logger logger = LoggerFactory.getLogger(MessageBoudleUtil.class);
	
	public static String getMessage(String key) {
		return getMessage(key, null);
	}

	public static String getMessage(String key, String... args) {
		ResourceBundle rb = ResourceBundle.getBundle(BASE_NAME);
		try {
			return loadArgs(rb.getString(key),  args);
		} catch (Exception mre) {
			logger.warn(mre.getMessage());
			return key;
		}
	}

	private static String loadArgs(String message, Object[] args){
		if (!StringUtils.isEmpty(message) && args != null){
			return  MessageFormat.format(message, args);
		}
		return message;
	}

}
