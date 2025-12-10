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
import com.pax.market.trdsys.sdk.base.request.SdkRequest;
import com.pax.market.trdsys.sdk.base.request.SdkRequest.RequestMethod;
import com.pax.market.trdsys.sdk.base.utils.JsonUtils;
import com.pax.market.trdsys.sdk.base.utils.MessageBoudleUtil;
import com.pax.market.trdsys.sdk.base.utils.StringUtils;
import com.pax.market.trdsys.sdk.message.dto.PushMessageCreateResultDto;
import com.pax.market.trdsys.sdk.message.dto.QueryArriveRateDto;
import com.pax.market.trdsys.sdk.message.request.MessageCreateRequest;
import com.pax.market.trdsys.sdk.message.request.SendMsgByTagRequest;
import com.pax.market.trdsys.sdk.message.request.SendRapidMessageRequest;
import com.pax.market.trdsys.sdk.message.request.SingleTerminalMsgCreateReqeust;
import com.pax.market.trdsys.sdk.message.response.BaseResponse;
import com.pax.market.trdsys.sdk.message.result.Result;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author tanjie
 * @date  2019/04/29 16:51:07
 */
public class CloudMessageServiceApi extends BaseApiClient{
	
	private static final String CREATE_PUSH_MESSAGE_URL = "/v1/3rd/cloudmsg";
	private static final String CREATE_PUSH_MESSAGE_4_SINGLE_TERMINAL_URL = "/v1/3rd/cloudmsg/single";
	private static final String SEND_RAPID_MESSAGE_URL = "/v1/3rd/cloudmsg/rapid";
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
	
	public Result<PushMessageCreateResultDto> sendRapidMessage(SendRapidMessageRequest request){
		List<String> validationErrors = new ArrayList<>();
		if(Objects.isNull(request)) {
			validationErrors.add(MessageBoudleUtil.getMessage("parameter.request.mandatory"));
			return new Result<>(validationErrors);
		}
		if(StringUtils.isEmpty(request.getSerialNo())) {
			validationErrors.add(MessageBoudleUtil.getMessage("parameter.serialNo.mandatory"));
		}
		if(StringUtils.isEmpty(request.getContent())) {
			validationErrors.add(MessageBoudleUtil.getMessage("parameter.content.mandatory"));
		}else if(request.getContent().getBytes().length > 1024) {
			validationErrors.add(MessageBoudleUtil.getMessage("parameter.content.too.long"));
		}
		if(!validationErrors.isEmpty()) {
			return new Result<>(validationErrors);
		}
		SdkRequest sdkRequest = new SdkRequest(SEND_RAPID_MESSAGE_URL);
		sdkRequest.setRequestMethod(RequestMethod.POST);
		sdkRequest.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
		sdkRequest.setRequestBody(new Gson().toJson(request, SendRapidMessageRequest.class));
		String resultJson = execute(sdkRequest);
		BaseResponse baseResponse = JsonUtils.fromJson(resultJson, BaseResponse.class);
		PushMessageCreateResultDto dto = JsonUtils.fromJson(resultJson, PushMessageCreateResultDto.class);
		Result<PushMessageCreateResultDto> result = new Result<>(baseResponse, dto);
		return result;
	}
	
	public Result<PushMessageCreateResultDto> createPushMessage(MessageCreateRequest createRequest) {
		if(createRequest == null) {
			List<String> validationErrors = new ArrayList<>();
			validationErrors.add(MessageBoudleUtil.getMessage("parameter.createRequest.mandatory"));
			return new Result<>(validationErrors);
		}
		if(createRequest.getSerialNos()!=null && createRequest.getSerialNos().length>MAX_SERIAL_NUMS) {
			List<String> validationErrors = new ArrayList<>();
			validationErrors.add(MessageBoudleUtil.getMessage("parameter.sns.max.size"));
			return new Result<>(validationErrors);
		}
		SdkRequest request = new SdkRequest(CREATE_PUSH_MESSAGE_URL);
		request.setRequestMethod(RequestMethod.POST);
		request.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
		request.setRequestBody(new Gson().toJson(createRequest, MessageCreateRequest.class));
		String resultJson = execute(request);
		BaseResponse baseResponse = JsonUtils.fromJson(resultJson, BaseResponse.class);
		PushMessageCreateResultDto dto = JsonUtils.fromJson(resultJson, PushMessageCreateResultDto.class);
		Result<PushMessageCreateResultDto> result = new Result<>(baseResponse, dto);
		return result;
	}

	public Result<PushMessageCreateResultDto> createPushMessageToSingleTerminal(SingleTerminalMsgCreateReqeust createRequest) {
		SdkRequest request = new SdkRequest(CREATE_PUSH_MESSAGE_4_SINGLE_TERMINAL_URL);
		request.setRequestMethod(RequestMethod.POST);
		request.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
		request.setRequestBody(new Gson().toJson(createRequest, SingleTerminalMsgCreateReqeust.class));
		String resultJson = execute(request);
		BaseResponse baseResponse = JsonUtils.fromJson(resultJson, BaseResponse.class);
		PushMessageCreateResultDto dto = JsonUtils.fromJson(resultJson, PushMessageCreateResultDto.class);
		Result<PushMessageCreateResultDto> result = new Result<>(baseResponse, dto);
		return result;
	}

	public Result<PushMessageCreateResultDto> createPushMessageByTag(SendMsgByTagRequest request) {
		if(request == null) {
			List<String> validationErrors = new ArrayList<>();
			validationErrors.add(MessageBoudleUtil.getMessage("parameter.request.mandatory"));
			return new Result<>(validationErrors);
		}
		if(StringUtils.isEmpty(request.getTagName())) {
			List<String> validationErrors = new ArrayList<>();
			validationErrors.add(MessageBoudleUtil.getMessage("tag.mandatory"));
			return new Result<>(validationErrors);
		}
		SdkRequest httpRequest = new SdkRequest(CREATE_PUSH_MESSAGE_BY_TAG_URL);
		httpRequest.setRequestMethod(RequestMethod.POST);
		httpRequest.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
		httpRequest.setRequestBody(new Gson().toJson(request, SendMsgByTagRequest.class));
		String resultJson = execute(httpRequest);
		BaseResponse baseResponse = JsonUtils.fromJson(resultJson, BaseResponse.class);
		PushMessageCreateResultDto dto = JsonUtils.fromJson(resultJson, PushMessageCreateResultDto.class);
		Result<PushMessageCreateResultDto> result = new Result<>(baseResponse, dto);
		return result;
	}

	public Result<QueryArriveRateDto> queryArriveRate(String messageIdentifier) {
		List<String> validationErrors = new ArrayList<>();
		if(StringUtils.isEmpty(messageIdentifier)) {
			validationErrors.add(MessageBoudleUtil.getMessage("parameter.identifier.mandatory"));
			return new Result<>(validationErrors);
		}
		if(messageIdentifier.trim().length()!=32) {
			validationErrors.add(MessageBoudleUtil.getMessage("parameter.identifier.length"));
			return new Result<>(validationErrors);
		}
		SdkRequest request = new SdkRequest(QUERY_ARRIVE_RATE_RUL.replace("{identifier}", messageIdentifier));
		request.setRequestMethod(RequestMethod.GET);
		request.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
		String resultJson = execute(request);
		BaseResponse baseResponse = JsonUtils.fromJson(resultJson, BaseResponse.class);
		QueryArriveRateDto dto = JsonUtils.fromJson(resultJson, QueryArriveRateDto.class);
		return new Result<>(baseResponse, dto);
	}


	public static void main(String[] args) {
		CloudMessageServiceApi api = new CloudMessageServiceApi("anull", "anull", "anull");
		SendRapidMessageRequest request = new SendRapidMessageRequest();
		JSONObject obj = new JSONObject();
		obj.put("transactionId", 1234567);
		obj.put("time", "2025-11-12 12:05:06");
		obj.put("amount", 10);
		request.setContent(obj);
		api.sendRapidMessage(request);
	}

}
