package com.pax.market.trdsys.sdk.base.constant;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessageMapping {
    private static final Map<String, String> message = new HashMap<String, String>(){{
        put("-2", "SDK parameter error!");
        put("-3", "SDK not initialed!");
        put("-5", "JSON error");
        put("-6", "Connection timeout!");
        put("-7", "Cannot connect to remote server!");
        put("-8", "Request error!");
        put("-9", "Extract zip file failed!");
        put("-10", "File MD5 not correct!");
        put("-11", "Replace paramVariables failed!");
        put("-12", "SDK initialize failed!");
        put("-13", "BaseUrl not correct!");
        put("parameter.identifier.mandatory", "Parameter messageIdentifier is mandatory!");
        put("parameter.identifier.length", "Parameter messageIdentifier's length must be 32!");
        put("parameter.createRequest.mandatory", "Parameter createRequest is mandatory!");
        put("parameter.sns.max.size", "Parameter serialNos's max size is 1000!");
        put("parameter.request.mandatory", "Parameter request is mandatory!");
        put("tag.mandatory", "Tag is mandatory!");
        put("parameter.effectiveDays.invalid", "Parameter effectiveDays must be 1, 3 or 5!");
    }};

    public static String getMsg(String key) {
        return message.get(key);
    }
}
