# PAXSTORE Cloud Message API Java SDK


<br/>
<br/>

## Introduction


Cloud Message is a service provided by PAXSTORE, it allows ISV to send messages to app of specified terminals.
PAXSTORE exposes RESTful API to let ISV to invoke. And this SDK simplifies the integration. 
Before using the service, PAXSTORE need to enable Cloud Message service. 
PAXSTORE exposes reseller, merchant and terminal related APIs for third-party system convenience. So that the authorized third-party system can do operations for reseller, merchant and terminal without logging into PAXSTORE's admin console. The exposed API is RESTful formatted. PAXSTORE provides the Java SDK to simplify the remote API invoke.  





<br>

## Precondition

There are some preconditions before using Cloud Message service.

* Cloud Message service is enabled in PAXSTORE
* App supports Cloud Message (Integrate with latest paxstore-3rd-app-android-sdk and claim this app support Cloud Message service when upload in PAXSTORE developer center)

* Cloud Message servie enabled by marketplace administrator at app level in app detail page

* Terminal has installed the latest PAXSTORE client


## Get Started  

### Integrate with SDK

Update pom.xml add SDK dependency for maven project.

```
<dependency>
    <groupId>com.whatspos.sdk</groupId>
    <artifactId>3rdsys-cloudmsg</artifactId>
    <version>1.6.0</version>
</dependency>
```

### API Usage

Initialize the API using the constructor like this.  
```
CloudMessageServiceApi api = new CloudMessageServiceApi("https://api.whatspos.com/p-market-api", "appkey", "appsecret");
```
Please use the correct app key and secret.

All the api in the class CloudMessageServiceApi returns a unified result *com.pax.market.trdsys.sdk.message.result.Result*
Below is the structure of class *com.pax.market.api.sdk.java.api.base.dto.Result*

|Property|Type|Description|
|:--|:--|:--|
|businessCode|int|The business code, it reprensent the API invoke result. 0 means invoke the API success, if it is -1 means SDK side validation failed. For other business codes please refer to the message property|
|message|String|The description of businessCode|
|validationErrors|List|Client side validation errors.|
|data|T(generic)|The actural DTO, the structure will be described in each APIs|


Below figure listed the global business codes, those business codes may appear in every result of API call. This document won't list those business codes in the following API chapters when introducing the APIs.

|Business Code|Message|Description|
|:--|:--|:--|
|0||Successful API call.|
|-5|JSON error!||
|-6|Connection timeout!|Encounter SocketTimeoutException.|
|-7|Cannot connect to remote server!|The remote server is down or the constructor argument *baseUrl* is not correct.|
|-8|Request error!|Please check the error log or send the error log to support.|
|-13|BaseUrl not correct!|The API BaseUrl may not correct|
|129|Authentication failed||
|104|Client key is missing or invalid||
|108|Marketplace is not available||
|109|Marketplace is not active||
|105|Client key is blocked||
|103|Access token is invalid||
|102|Access token is missing|&nbsp;|
|101|Invalid request method|The request method is not correct|
|113|Request parameter is missing or invalid||
|997|Malformed or illegal request|The JSON in request body is not a valid JSON|
|998|Bad request||
|999|Unknown error|Unknow error, please contact with support.|





#### Send Message to Terminals

##### API  
```  
public Result<PushMessageCreateResultDto> createPushMessage(MessageCreateRequest createRequest)
```

The structure of the class *PushMessageCreateResultDto*  

|Property Name|Type|Description|
|:---|:---|:---|
|msgIdentifier|String|The identify of the message|



Currently Cloud Message service support 3 types of message.

* Notification Type  
  For notification message type the message body contains a title and content like below

```
{
  "title":"This is message title",
  "content":"This is message content"
}
```

* Data Type  
  Data type message body is a json format string

* Mix Type
Mix type message is the combination of notification type message and data type message.

After send message api will return the result, if the property businessCode of result is 0 mean the message is created successfully.


##### Sample Code  

* Send notification type message to terminal sn00001,sn00002 and print the msgIdentify if success  
```  
MessageCreateRequest request = new MessageCreateRequest();
request.setMsgType(MsgType.Notification);
request.setEffectiveDays(3);
MsgContent msgContent = new MsgContent();
Notification notification = new Notification();
notification.setTitle("This is title of notification");
notification.setContent("This is content of notification");
msgContent.setNotification(notification);
request.setContent(msgContent);
request.setSerialNos(new String[]{"sn00001", "sn00002"});
Result<PushMessageCreateResultDto> result = getMessageApi().createPushMessage(request);
if(result.getData()!=null) {
	System.out.println(result.getData().getMsgIdentifier());
}
```


* Send data type message to terminal sn00001,sn00002 and print the msgIdentify if success  
```  
MessageCreateRequest request = new MessageCreateRequest();
request.setMsgType(MsgType.Data);
MsgContent msgContent = new MsgContent();
JSONObject data = new JSONObject();
data.put("date", "12/7/2019");
data.put("titile", "title");
JSONObject addressNode = new JSONObject();
addressNode.put("city", "jiangsu suzh");
addressNode.put("postCode", "215000");
data.put("address", addressNode);
msgContent.setData(data);
request.setContent(msgContent);
request.setSerialNos(new String[]{"sn00001", "sn00002"});
Result<PushMessageCreateResultDto> result = getMessageApi().createPushMessage(request);
if(result.getData()!=null) {
	System.out.println(result.getData().getMsgIdentifier());
}
```

* Send mix type message to terminal sn00001,sn00002 and print the msgIdentify if success  
```  
MessageCreateRequest request = new MessageCreateRequest();
request.setMsgType(MsgType.Mix);
MsgContent msgContent = new MsgContent();
Notification notification = new Notification();
notification.setTitle("This is title of notification");
notification.setContent("This is content of notification");
msgContent.setNotification(notification);
JSONObject data = new JSONObject();
data.put("date", "12/7/2019");
data.put("titile", "title");
JSONObject addressNode = new JSONObject();
addressNode.put("city", "jiangsu suzh");
addressNode.put("postCode", "215000");
data.put("address", addressNode);
msgContent.setData(data);
request.setContent(msgContent);
request.setSerialNos(new String[]{"sn00001", "sn00002"});
Result<PushMessageCreateResultDto> result = getMessageApi().createPushMessage(request);
if(result.getData()!=null) {
	System.out.println(result.getData().getMsgIdentifier());
}
```


**Possible client validation errors**
> <font color=red>Parameter createRequest is mandatory!</font>  
> <font color=red>Parameter serialNos's max size is 1000!</font>  

**Possible business codes**

|Business Code|Message|Description|
|:---|:---|:---|
|2609|Serial No is mandatory|&nbsp;|
|2610|Max terminal Id size is 1000|&nbsp;|
|2621|Duplicated serial numbers|&nbsp;|
|2613|Message type in invalid|&nbsp;|
|2611|Message content is mandatory|&nbsp;|
|2617|Message data is mandatory|If the message type is data or mix type, the data property of create reqeust is mandatory|
|2618|Message data is invalid|If the message type is data or mix type, make sure the data is a json object|
|2612|Message content is too long|&nbsp;|
|2619|No valid serial numbers|Make sure the terminal has installed the app and the push channel has switched to cloud push channel|

#### Send Message to Single Terminal

##### API  
```  
public Result<PushMessageCreateResultDto> createPushMessageToSingleTerminal(SingleTerminalMsgCreateReqeust createRequest)
```

* Sample codes of sending notification type message to a terminal  

```
SingleTerminalMsgCreateReqeust request = new SingleTerminalMsgCreateReqeust();
request.setMsgType(MsgType.Notification);
MsgContent msgContent = new MsgContent();
Notification notification = new Notification();
notification.setTitle("This is title");
notification.setContent("content");
msgContent.setNotification(notification);
request.setContent(msgContent);
request.setSerialNo("1850000874");
Result<PushMessageCreateResultDto> result = getMessageApi().createPushMessageToSingleTerminal(request);
```
#### Send Data Type Message to Single Terminal  

##### API  
```
public Result<PushMessageCreateResultDto> sendRapidMessage(SendRapidMessageRequest request)
```  

* Sample codes of sending data type message to single terminal

```
SendRapidMessageRequest request = new SendRapidMessageRequest();
request.setSerialNo("0820534806");
JSONObject content = new JSONObject();
content.put("transactionIdl", "123456789");
content.put("billingAddress", "Jiangsu Suzhou SIP Park B205");
content.put("amount", 1024);
content.put("quantity", 10);
request.setContent(content);
Result<PushMessageCreateResultDto> result = getMessageApi().sendRapidMessage(request);
```
**Possible client validation errors**
> <font color=red>Parameter request is mandatory!</font>  
> <font color=red>Parameter serialNo is mandatory!</font>  
> <font color=red>Parameter content is mandatory!</font>  
> <font color=red>Message content cannot greater than 1024 byte!</font>  

##### Description
This API will send data type message to terminal. The default API limit for this API is 500 per minute. The message content length is 1024 byte. 


#### Send Message to terminal(s) by tag

##### API

```
public Result<PushMessageCreateResultDto> createPushMessageByTag(SendMsgByTagRequest request)
```

* Sample codes of sending notification type message to terminal(s) by tag  

```
SendMsgByTagRequest request = new SendMsgByTagRequest();
request.setMsgType(MsgType.Notification);
MsgContent msgContent = new MsgContent();
Notification notification = new Notification();
notification.setTitle("This is title of notification");
notification.setContent("This is content of notification");
msgContent.setNotification(notification);
request.setContent(msgContent);
request.setTagName("volvo");
Result<PushMessageCreateResultDto> result = getMessageApi().createPushMessageByTag(request);
```

**Possible client validation errors**
> <font color=red>Parameter request is mandatory!</font>  
> <font color=red>Tag is mandatory!</font>  


#### Query the message arrived number


##### API  
```  
public Result<QueryArriveRateDto> queryArriveRate(String messageIdentifier)
```
This API is used to query the message arrive terminal number. The parameter messageIdentifier is returned after created message success. The effective time of the message is 24 hours, and query the message arrived terminal number when message is still effective is not allowed. The structure of QueryArriveRateDto like below

|Property Name|Type|Description|
|:---|:---|:---|
|arrivedNumber|Integer|The number of terminal of the message arrived|
|arrivedRate|String|The percentage of the arrived terminal number|

##### Sample Code

```
Result<QueryArriveRateDto> result = getMessageApi().queryArriveRate("b8ddeca556dd4f25b1a1143a3f5e8855");
if(result.getBusinessCode() == 0) {
	System.out.println(result.getData().getArrivedNumber());
	System.out.println(result.getData().getArrivedRate());
}
```


**Possible client validation errors**
> <font color=red>Parameter messageIdentifier is mandatory!</font>  
> <font color=red>Parameter messageIdentifier's length must be 32!</font>  


**Possible business codes**

|Business Code|Message|Description|
|:---|:---|:---|
|2615|Invalid message identifier|&nbsp;|
|2620|Message sent failed|&nbsp;|
|2616|Arrived rate is not available now, please try later|&nbsp;|










## License

See the [Apache 2.0 license](LICENSE) file for details.

    Copyright 2018 PAX Computer Technology(Shenzhen) CO., LTD ("PAX")
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at following link.
    
         http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

<br/>


## Changelog  

### 1.1.0  

Support send to message to sandbox terminal and normal terminal separately. The API send the message to normal terminal by default. If user want to send the message to sandbox terminal please call the method *setSendToSandboxTerminal(true)* in the class *MessageCreateRequest*.

Please note this feature is supported by PAXSTORE version 7.4.0 or later.

### 1.2.0

Support send message by terminal'sTID. To send message by TID please call the method *setSendByTid(true)* in the class *MessageCreateRequest* and call the method *setTids(String[] tids)* to set the TIDs.

Please note this feature is supported by PAXSTORE version 8.1.0 or later.

### 1.3.0  
Support send message to single terminal, and the max send rate is 120 times per minutes based on app key.  

### 1.4.0  
Support send message by tag

### 1.5.0  
Support specify the message effective days, the default value is 1 day, the valid value is 1 day or 3 days.

### 1.6.0
Add new API to send data type message to single terminal. The default API rate limit for this new API is 500 per minute.



