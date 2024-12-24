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

import com.google.gson.Gson;
import com.pax.market.trdsys.sdk.base.constant.ErrorMessageMapping;
import com.pax.market.trdsys.sdk.base.constant.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tanjie
 * @date 2018-07-04
 */


/**
 * The type Json utils.
 */
public class JsonUtils {
	private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static Gson gson = getGson();

    /**
     * Gets gson.
     *
     * @return the gson
     */
    static /*package*/ Gson getGson() {
        if (gson == null) {
            synchronized (Gson.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

    /**
     * 根据javaBean生成Json对象格式字符串
     *
     * @param object 任意javaBean类型对象
     * @return 拼接好的String对象 string
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * 根据Sdk返回的Json字符串生成Javabean，json字符串封装在data中
     *
     * @param <T>        the type parameter
     * @param sdkJsonStr Json字符串
     * @param clazz      the clazz
     * @return Javabean对象 t
     */
    public static <T> T fromJson(String sdkJsonStr, Class<T> clazz) {
    	logger.debug(sdkJsonStr);
        return gson.fromJson(sdkJsonStr, clazz);
    }

    /**
     * Gets sdk json.
     *
     * @param resultCode the result code
     * @return the sdk json
     */
    public static String getSdkJson(int resultCode) {
        String message = "";
        switch (resultCode) {
            case ResultCode.SDK_PARAM_ERROR:
                message = "-2";
                break;
            case ResultCode.SDK_UNINIT:
                message = "-3";
                break;
            case ResultCode.SDK_DEC_ERROR:
                message = "-4";
                break;
            case ResultCode.SDK_JSON_ERROR:
                message = "-5";
                break;
            case ResultCode.SDK_CONNECT_TIMEOUT:
                message = "-6";
                break;
            case ResultCode.SDK_UN_CONNECT:
                message = "-7";
                break;
            case ResultCode.SDK_RQUEST_EXCEPTION:
                message = "-8";
                break;
            case ResultCode.SDK_UNZIP_FAILED:
                message = "-9";
                break;
            case ResultCode.SDK_MD_FAILED:
                message = "-10";
                break;
            case ResultCode.SDK_REPLACE_VARIABLES_FAILED:
                message = "-11";
                break;
            case ResultCode.SDK_INIT_FAILED:
                message = "-12";
                break;
            case ResultCode.SDK_FILE_NOT_FOUND:
                message = "-13";
                break;

        }
        message = ErrorMessageMapping.getMsg(message);
        return getSdkJson(resultCode, message);
    }

    /**
     * Gets sdk json.
     *
     * @param resultCode the result code
     * @param message    the message
     * @return the sdk json
     */
    public static String getSdkJson(int resultCode, String message) {

    	Map<String, Object> result = new HashMap<String, Object>();
        result.put("businessCode", resultCode);
        result.put("message", message);
        return toJson(result);
    }


}