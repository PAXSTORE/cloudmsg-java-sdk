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
package com.pax.market.trdsys.sdk.base;



import com.pax.market.trdsys.sdk.base.constant.Constants;
import com.pax.market.trdsys.sdk.base.constant.ResultCode;
import com.pax.market.trdsys.sdk.base.request.SdkRequest;
import com.pax.market.trdsys.sdk.base.utils.JsonUtils;
import com.pax.market.trdsys.sdk.base.utils.HttpUtils;
import com.pax.market.trdsys.sdk.base.utils.alg.CryptoUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * 客户端
 */
public class BaseApiClient {

	private static final Logger logger = LoggerFactory.getLogger(BaseApiClient.class.getSimpleName());

    /**
     * The Base url.
     */
    protected String baseUrl;
    /**
     * The Api key.
     */
    protected String apiKey;
    /**
     * The Api secret.
     */
    protected String apiSecret;
    /**
     * The Sign method.
     */
    protected String signMethod = Constants.SIGN_METHOD_HMAC;
    /**
     * The Connect timeout.
     */
    protected int connectTimeout = 5000;
    /**
     * The Read timeout.
     */
    protected int readTimeout = 15000;
    
    private boolean signRequestBody = true;
    
    private String signatureHeaderName = Constants.SIGNATURE;


    /**
     * Instantiates a new Default client.
     *
     * @param baseUrl   the base url
     * @param apiKey    the app key
     * @param apiSecret the app secret
     */
    public BaseApiClient(String baseUrl, String apiKey, String apiSecret) {
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		if(baseUrl.endsWith("/")) {
    		this.baseUrl = baseUrl.substring(0, baseUrl.length()-1);
    	}else {
    		this.baseUrl = baseUrl;
    	}
	}
    

    /**
     * Instantiates a new Default client.
     *
     * @param baseUrl        the base url
     * @param apiKey         the app key
     * @param apiSecret      the app secret
     * @param connectTimeout the connect timeout
     * @param readTimeout    the read timeout
     */
    public BaseApiClient(String baseUrl, String apiKey, String apiSecret, int connectTimeout, int readTimeout) {
		this(baseUrl, apiKey, apiSecret);
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}


    /**
     * Execute string.
     *
     * @param request the request
     * @return the string
     */
    protected String execute(SdkRequest request) {
    	request.addHeader(Constants.REQ_HEADER_SDK_LANG, Constants.THIRD_PARTY_API_SDK_LANGUAGE);
    	request.addHeader(Constants.REQ_HEADER_SDK_VERSION, Constants.THIRD_PARTY_API_SDK_VERSION);
		try {
			return _execute(request);
		} catch (IOException e) {
			logger.error("IOException occurred when execute request", e);
		} catch (GeneralSecurityException e) {
			logger.error("GeneralSecurityException occurred when execute request.", e);
		}
		return JsonUtils.getSdkJson(ResultCode.SDK_RQUEST_EXCEPTION);
	}

	protected String _execute(SdkRequest request) throws IOException, GeneralSecurityException {
		String response;
		Long timestamp = request.getTimestamp();
		if(timestamp == null){
			timestamp = System.currentTimeMillis();
		}
		request.addRequestParam(Constants.TIMESTAMP, Long.toString(timestamp));
		String query = HttpUtils.buildQuery(request.getRequestParams(), Constants.CHARSET_UTF8);
		if(apiSecret != null) {
			String signature = CryptoUtils.signRequest(query, signRequestBody?request.getRequestBody():null, apiSecret, signMethod);
			request.addHeader(signatureHeaderName, signature);
		}
		String requestUrl = HttpUtils.buildRequestUrl(baseUrl + request.getRequestMappingUrl(), query);
		logger.info(" --> {} {}", request.getRequestMethod().getValue(), requestUrl);
		response = HttpUtils.request(requestUrl, request.getRequestMethod().getValue(), connectTimeout, readTimeout, request.getRequestBody(), request.getHeaderMap());
		return response;
	}

    /**
     * 设置API请求的连接超时时间
     *
     * @param connectTimeout the connect timeout
     */
    public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

    /**
     * 设置API请求的读超时时间
     *
     * @param readTimeout the read timeout
     */
    public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

}
