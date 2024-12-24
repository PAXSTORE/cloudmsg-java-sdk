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
package com.pax.market.trdsys.sdk.message;


import com.google.gson.Gson;
import com.pax.market.trdsys.sdk.base.BaseApiClient;
import com.pax.market.trdsys.sdk.base.constant.Constants;
import com.pax.market.trdsys.sdk.base.constant.ErrorMessageMapping;
import com.pax.market.trdsys.sdk.base.request.SdkRequest;
import com.pax.market.trdsys.sdk.base.request.SdkRequest.RequestMethod;
import com.pax.market.trdsys.sdk.base.utils.JsonUtils;
import com.pax.market.trdsys.sdk.base.utils.StringUtils;
import com.pax.market.trdsys.sdk.message.dto.PushMessageCreateResultDto;
import com.pax.market.trdsys.sdk.message.dto.QueryArriveRateDto;
import com.pax.market.trdsys.sdk.message.request.MessageCreateRequest;
import com.pax.market.trdsys.sdk.message.request.SendMsgByTagRequest;
import com.pax.market.trdsys.sdk.message.request.SingleTerminalMsgCreateReqeust;
import com.pax.market.trdsys.sdk.message.response.BaseResponse;
import com.pax.market.trdsys.sdk.message.result.Result;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tanjie
 * @date  2019/04/29 16:51:07
 */
public class CloudMessageServiceApi extends BaseApiClient{
	
	private static final String CREATE_PUSH_MESSAGE_URL = "/v1/3rd/cloudmsg";
	private static final String CREATE_PUSH_MESSAGE_4_SINGLE_TERMINAL_URL = "/v1/3rd/cloudmsg/single";
	private static final String CREATE_PUSH_MESSAGE_BY_TAG_URL = "/v1/3rd/cloudmsg/bytag";
	private static final String QUERY_ARRIVE_RATE_RUL = "/v1/3rd/cloudmsg/{identifier}";
	private static final int MAX_SERIAL_NUMS = 1000;
	
	public CloudMessageServiceApi(String baseUrl, String appKey, String apiSecret) {
		super(baseUrl, appKey, apiSecret);
	}
	
	protected String execute(SdkRequest request) {
		request.addRequestParam(Constants.APP_KEY, apiKey);
		return super.execute(request);
	}
	

	public Result<PushMessageCreateResultDto> createPushMessage(MessageCreateRequest createRequest) {
		if(createRequest == null) {
			List<String> validationErrors = new ArrayList<String>();
			validationErrors.add(ErrorMessageMapping.getMsg("parameter.createRequest.mandatory"));
			return new Result<PushMessageCreateResultDto>(validationErrors);
		}
		List<String> validationErrors = new ArrayList<String>();
		if(createRequest.getSerialNos()!=null && createRequest.getSerialNos().length>MAX_SERIAL_NUMS) {
			validationErrors.add(ErrorMessageMapping.getMsg("parameter.sns.max.size"));
		}
		if(!validateEffectiveDay(createRequest.getEffectiveDays())) {
			validationErrors.add(ErrorMessageMapping.getMsg("parameter.effectiveDays.invalid"));
		}
		if(!validationErrors.isEmpty()){
			return new Result<PushMessageCreateResultDto>(validationErrors);
		}
		SdkRequest request = new SdkRequest(CREATE_PUSH_MESSAGE_URL);
		request.setRequestMethod(RequestMethod.POST);
		request.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
		request.setRequestBody(new Gson().toJson(createRequest, MessageCreateRequest.class));
		String resultJson = execute(request);
		BaseResponse baseResponse = JsonUtils.fromJson(resultJson, BaseResponse.class);
		PushMessageCreateResultDto dto = JsonUtils.fromJson(resultJson, PushMessageCreateResultDto.class);
		Result<PushMessageCreateResultDto> result = new Result<PushMessageCreateResultDto>(baseResponse, dto);
		return result;
	}

	public Result<PushMessageCreateResultDto> createPushMessageToSingleTerminal(SingleTerminalMsgCreateReqeust createRequest) {
		List<String> validationErrors = new ArrayList<String>();
		if(createRequest == null) {
			validationErrors.add(ErrorMessageMapping.getMsg("parameter.createRequest.mandatory"));
			return new Result<PushMessageCreateResultDto>(validationErrors);
		}
		if(!validateEffectiveDay(createRequest.getEffectiveDays())) {
			validationErrors.add(ErrorMessageMapping.getMsg("parameter.effectiveDays.invalid"));
		}
		if(!validationErrors.isEmpty()){
			return new Result<PushMessageCreateResultDto>(validationErrors);
		}
		SdkRequest request = new SdkRequest(CREATE_PUSH_MESSAGE_4_SINGLE_TERMINAL_URL);
		request.setRequestMethod(RequestMethod.POST);
		request.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
		request.setRequestBody(new Gson().toJson(createRequest, SingleTerminalMsgCreateReqeust.class));
		String resultJson = execute(request);
		BaseResponse baseResponse = JsonUtils.fromJson(resultJson, BaseResponse.class);
		PushMessageCreateResultDto dto = JsonUtils.fromJson(resultJson, PushMessageCreateResultDto.class);
		Result<PushMessageCreateResultDto> result = new Result<PushMessageCreateResultDto>(baseResponse, dto);
		return result;
	}

	public Result<PushMessageCreateResultDto> createPushMessageByTag(SendMsgByTagRequest request) {
		if(request == null) {
			List<String> validationErrors = new ArrayList<String>();
			validationErrors.add(ErrorMessageMapping.getMsg("parameter.request.mandatory"));
			return new Result<PushMessageCreateResultDto>(validationErrors);
		}
		List<String> validationErrors = new ArrayList<String>();
		if(StringUtils.isEmpty(request.getTagName())) {
			validationErrors.add(ErrorMessageMapping.getMsg("tag.mandatory"));
			return new Result<PushMessageCreateResultDto>(validationErrors);
		}
		if(!validateEffectiveDay(request.getEffectiveDays())) {
			validationErrors.add(ErrorMessageMapping.getMsg("parameter.effectiveDays.invalid"));
		}
		if(!validationErrors.isEmpty()){
			return new Result<PushMessageCreateResultDto>(validationErrors);
		}
		SdkRequest httpRequest = new SdkRequest(CREATE_PUSH_MESSAGE_BY_TAG_URL);
		httpRequest.setRequestMethod(RequestMethod.POST);
		httpRequest.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
		httpRequest.setRequestBody(new Gson().toJson(request, SendMsgByTagRequest.class));
		String resultJson = execute(httpRequest);
		BaseResponse baseResponse = JsonUtils.fromJson(resultJson, BaseResponse.class);
		PushMessageCreateResultDto dto = JsonUtils.fromJson(resultJson, PushMessageCreateResultDto.class);
		Result<PushMessageCreateResultDto> result = new Result<PushMessageCreateResultDto>(baseResponse, dto);
		return result;
	}

	public Result<QueryArriveRateDto> queryArriveRate(String messageIdentifier) {
		List<String> validationErrors = new ArrayList<String>();
		if(StringUtils.isEmpty(messageIdentifier)) {
			validationErrors.add(ErrorMessageMapping.getMsg("parameter.identifier.mandatory"));
			return new Result<QueryArriveRateDto>(validationErrors);
		}
		if(messageIdentifier.trim().length()!=32) {
			validationErrors.add(ErrorMessageMapping.getMsg("parameter.identifier.length"));
			return new Result<QueryArriveRateDto>(validationErrors);
		}
		SdkRequest request = new SdkRequest(QUERY_ARRIVE_RATE_RUL.replace("{identifier}", messageIdentifier));
		request.setRequestMethod(RequestMethod.GET);
		request.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
		String resultJson = execute(request);
		BaseResponse baseResponse = JsonUtils.fromJson(resultJson, BaseResponse.class);
		QueryArriveRateDto dto = JsonUtils.fromJson(resultJson, QueryArriveRateDto.class);
		return new Result<QueryArriveRateDto>(baseResponse, dto);
	}

	private boolean validateEffectiveDay(int effectiveDay) {
		if(effectiveDay != 1 || effectiveDay != 3 || effectiveDay !=5) {
			return false;
		}else{
			return true;
		}
	}

}
