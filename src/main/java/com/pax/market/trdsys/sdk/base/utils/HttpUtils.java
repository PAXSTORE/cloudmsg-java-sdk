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


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pax.market.trdsys.sdk.base.constant.Constants;
import com.pax.market.trdsys.sdk.base.constant.ResultCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;


/**
 * 网络工具类。
 */
public abstract class HttpUtils {
	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	private static final String HEADER_RATE_LIMIT_TOTAL = "X-RateLimit-Limit";
	private static final String HEADER_RATE_LIMIT_REMAINING = "X-RateLimit-Remaining";
	private static final String HEADER_RATE_LIMIT_RESET = "X-RateLimit-Reset";

	private static JsonParser jsonParser = new JsonParser();

	private HttpUtils() {
	}

    /**
     * Request string.
     *
     * @param requestUrl     the request url
     * @param requestMethod  the request method
     * @param connectTimeout the connect timeout
     * @param readTimeout    the read timeout
     * @param userData       the user data
     * @param headerMap      the header map
     * @return the string
     */
    public static String request(String requestUrl, String requestMethod, int connectTimeout, int readTimeout, String userData, Map<String, String> headerMap){
		return request(requestUrl, requestMethod, connectTimeout, readTimeout, userData, false, headerMap);
	}


	private static String request(String requestUrl, String requestMethod, int connectTimeout, int readTimeout, String userData, boolean compressData,
								  Map<String, String> headerMap) {
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = getConnection(requestUrl, connectTimeout, readTimeout);
			return finalRequest(urlConnection, requestMethod, userData, compressData, headerMap);
		} catch (Exception e) {
			logger.error("Exception Occurred", e);
			return JsonUtils.getSdkJson(ResultCode.SDK_RQUEST_EXCEPTION);
		} finally {
			if(urlConnection != null) {
				urlConnection.disconnect();
			}
		}
	}

	private static String finalRequest(HttpURLConnection urlConnection, String requestMethod, String userData, boolean compressData,
									   Map<String, String> headerMap) {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		FileOutputStream fileOutputStream = null;
		try {
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestMethod(requestMethod);
			if (headerMap != null) {
				for (Entry<String, String> entry : headerMap.entrySet()) {
					urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			if ("GET".equalsIgnoreCase(requestMethod) || "DELETE".equalsIgnoreCase(requestMethod)) {
				urlConnection.connect();
			} else {
				urlConnection.setDoOutput(true);
			}
			String rateLimit = "";
			String rateLimitRemain = "";
			String rateLimitReset = "";
			if ((null != userData) && (userData.length() > 0)) {
				OutputStream outputStream = null;
				try {
					outputStream = urlConnection.getOutputStream();
					outputStream.write(userData.getBytes(Constants.CHARSET_UTF8));
				} finally {
					if (outputStream != null) {
						outputStream.close();
					}
				}
			}
			Map<String, List<String>> map = urlConnection.getHeaderFields();
			rateLimit = map.get(HEADER_RATE_LIMIT_TOTAL)==null?"":map.get(HEADER_RATE_LIMIT_TOTAL).get(0);
			rateLimitRemain = map.get(HEADER_RATE_LIMIT_REMAINING)==null?"":map.get(HEADER_RATE_LIMIT_REMAINING).get(0);
			rateLimitReset = map.get(HEADER_RATE_LIMIT_RESET)==null?"":map.get(HEADER_RATE_LIMIT_RESET).get(0);
			List<String> contentTypeHeaders = map.get(Constants.CONTENT_TYPE);
			String contentType = contentTypeHeaders!=null && contentTypeHeaders.size()>0?contentTypeHeaders.get(0):"";

			if (urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201
					|| urlConnection.getResponseCode() == 204) {
				bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), Constants.CHARSET_UTF8));
			} else {
				if(urlConnection.getErrorStream() != null) {
					bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(), Constants.CHARSET_UTF8));
				} else {
					bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), Constants.CHARSET_UTF8));
				}
			}

			String str;
			while ((str = bufferedReader.readLine()) != null) {
				stringBuilder.append(str);
			}
			String resultStr = stringBuilder.toString();
			if(contentType == null || !contentType.toLowerCase().contains("json")) {
				logger.error("Content-type is {}", contentType);
				logger.error("Response is: {}", resultStr);
				return JsonUtils.getSdkJson(ResultCode.SDK_RQUEST_EXCEPTION);
			}
			JsonObject json = jsonParser.parse(resultStr).getAsJsonObject();
			json.addProperty("rateLimit", rateLimit);
			json.addProperty("rateLimitRemain", rateLimitRemain);
			json.addProperty("rateLimitReset", rateLimitReset);
			return json.toString();
		} catch (SocketTimeoutException localSocketTimeoutException) {
			logger.error("SocketTimeoutException Occurred", localSocketTimeoutException);
			return JsonUtils.getSdkJson(ResultCode.SDK_CONNECT_TIMEOUT);
		} catch (ConnectException localConnectException) {
			logger.error("ConnectException Occurred", localConnectException);
			return JsonUtils.getSdkJson(ResultCode.SDK_UN_CONNECT);
		} catch (Exception ignored) {
			logger.error("Exception Occurred", ignored);
		} finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					logger.error("IOException Occurred", e);
				}
			}
			if(fileOutputStream != null){
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					logger.error("IOException Occurred", e);
				}
			}
			if(urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return JsonUtils.getSdkJson(ResultCode.SDK_RQUEST_EXCEPTION);
	}

	private static HttpURLConnection getConnection(String requestUrl, int connectTimeout, int readTimeout) throws IOException {
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);
		return conn;
	}


    /**
     * Build request url string.
     *
     * @param url     the url
     * @param queries the queries
     * @return the string
     */
    public static String buildRequestUrl(String url, String... queries) {
		if (queries == null || queries.length == 0) {
			return url;
		}

		StringBuilder newUrl = new StringBuilder(url);
		boolean hasQuery = url.contains("?");
		boolean hasPrepend = url.endsWith("?") || url.endsWith("&");

		for (String query : queries) {
			if (!StringUtils.isEmpty(query)) {
				if (!hasPrepend) {
					if (hasQuery) {
						newUrl.append("&");
					} else {
						newUrl.append("?");
						hasQuery = true;
					}
				}
				newUrl.append(query);
				hasPrepend = false;
			}
		}
		return newUrl.toString();
	}

    /**
     * Build query string.
     *
     * @param params  the params
     * @param charset the charset
     * @return the string
     * @throws IOException the io exception
     */
    public static String buildQuery(Map<String, String> params, String charset) throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> entries = params.entrySet();
		boolean hasParam = false;

		for (Entry<String, String> entry : entries) {
			String name = entry.getKey();
			String value = entry.getValue();
			// 忽略参数名或参数值为空的参数
			if (StringUtils.areNotEmpty(name, value)) {
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}

				query.append(name).append("=").append(URLEncoder.encode(value, charset));
			}
		}

		return query.toString();
	}

}
