package com.qingzi.api.meeting;

import com.qingzi.interfaces.API;
import com.qingzi.process.QZ;
import com.qingzi.system.MyRequest;
import com.qingzi.testUtil.MapUtil;
import com.qingzi.testUtil.RequestDataUtils;
import com.qingzi.testUtil.StringUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;

/**
 * @ClassName: dataUpload
 * @Description: 音视频上行数据上报
 * @author: wff
 * @date: 2022年4月22日17:02:32
 * @Copyright:
 */
public class dataUpload extends QZ implements API {

    public String parameter; //参数集合
    public String dataVideoAudio; //数据
    public String accountId; //crystal的用户id


    @Override
    public void initialize(HashMap<String, Object> data) {

    }

    @Override
    public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
        parameter = MapUtil.getValue("parameter", data);

        dataVideoAudio = MapUtil.getParameter(parameter, "data").trim();
        accountId = MapUtil.getParameter(parameter, "accountId").trim();
        if (!dataVideoAudio.equals("") && dataVideoAudio.equals("code")) {

            dataVideoAudio = "{\n" +
                    "    \"headerInfo\":{ \n" +
                    "            \"dataSchema\":\"upstream_info\",\n" +
                    "            \"eventId\":1,\n" +
                    "            \"eventName\":\"upstream_report\",\n" +
                    "            \"logTime\":\"1231231231\",\n" +
                    "            \"roomId\":\"1231231\",\n" +
                    "            \"peerId\":\"1231\",\n" +
                    "            \"dataSource\": \"demo-ff\" , \n" +
                    "            \"sessionId\":\"0ccbaa01-1629300267175\"\n" +
                    "    },\n" +
                    "    \"upBodyInfo\":{\n" +
                    "        \"userId\":\"12312\",\n" +
                    "        \"deviceType\":\"1\",\n" +
                    "        \"deviceVersion\":\"1.1.1\",\n" +
                    "        \"networkType\":3, \n" +
                    "        \"device\":\"ios\",\n" +
                    "        \"sdkVersion\":\"1231\",\n" +
                    "        \"clientCpuRate\":\"0.1\",\n" +
                    "        \"systemCpuRate\":\"0.2\",\n" +
                    "        \"systemCpuFrequencyRate\":\"0.3\",\n" +
                    "        \"roomId\":\"12312\",\n" +
                    "        \"bitrateSend\": \"2.1\",\n" +
                    "        \"bytesSent\":\"123\",\n" +
                    "        \"availableOutgoingBitrate\":\"1.2\",\n" +
                    "        \"packetsLost\":\"323424\",\n" +
                    "        \"packetsSent\":\"3244\",\n" +
                    "        \"roundTripTime\":\"1.2\",\n" +
                    "        \"fractionLost\":\"1.2\",\n" +
                    "        \"audioInfos\":[\n" +
                    "            {\n" +
                    "               \"kind\":\"mic\",\n" +
                    "               \"ssrc\":\"1234\",\n" +
                    "               \"bitrateSend\": \"12.2\",\n" +
                    "               \"packetsSent\":\"12313\",\n" +
                    "               \"audioLevel\":\"1.2\",\n" +
                    "               \"fractionLost\":\"0.2\",\n" +
                    "               \"roundTripTime\":\"1.2\",\n" +
                    "               \"audioVolume\":\"0.1\",\n" +
                    "               \"produceId\":\"adasdfasdfas12312\",\n" +
                    "               \"audioCodec\":{\n" +
                    "                    \"payloadType\":1,\n" +
                    "                    \"mimeType\":\"opus\",\n" +
                    "                    \"clockRate\":1,\n" +
                    "                    \"channels\":1\n" +
                    "                }\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"videoInfos\":[\n" +
                    "            {\n" +
                    "              \"kind\":\"webcam\",\n" +
                    "              \"ssrc\":\"123214\",\n" +
                    "              \"frameWidth\":5,\n" +
                    "              \"frameHeight\":500,\n" +
                    "              \"framesEncoded\":100,\n" +
                    "              \"framesSent\":10,\n" +
                    "              \"firCount\":1,\n" +
                    "              \"pliCount\":2,\n" +
                    "              \"nackCount\":2,\n" +
                    "              \"packetsSent\":123,\n" +
                    "              \"bytesSent\":234,\n" +
                    "              \"totalPacketSendDelay\":1.0,\n" +
                    "              \"retransmittedBitrate\":1.2,\n" +
                    "              \"framesPerSecond\": 1.2,\n" +
                    "              \"qualityLimitationReason\":\"XXX\",\n" +
                    "              \"fractionLost\":\"0.1\",\n" +
                    "              \"roundTripTime\":\"0.1\",\n" +
                    "              \"bitrateSend\": \"12.2\",\n" +
                    "              \"qp\":\"1.2\",\n" +
                    "              \"targetDelayMs\":\"100\",\n" +
                    "              \"retransmittedBytesSent\":213,\n" +
                    "              \"produceId\":\"adasdfasdfas12312\",\n" +
                    "              \"videoCodec\":{\n" +
                    "                    \"payloadType\":1,\n" +
                    "                    \"mimeType\":\"opus\",\n" +
                    "                    \"clockRate\":1,\n" +
                    "                    \"channels\":1\n" +
                    "                }\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "}";
            parameter = parameter.replace("\"data\":code", "\"data\":[" + dataVideoAudio + "]");
        }
        if (!accountId.equals("") && accountId.equals("code")) {
            accountId = userAccountId;
            parameter = parameter.replace("\"accountId\":code", "\"accountId\":\"" + accountId + "\"");
        }

        data.put("parameter", parameter);
        return data;
    }

    @Override
    public Response SendRequest(HashMap<String, String> headers, HashMap<String, Object> data, String Url,
                                String Request) {
        MyRequest myRequest = new MyRequest();
//		myRequest.setUrl(Url + "?userAccountId="+ userAccountId);
        myRequest.setUrl(Url);
        myRequest.setHeaders(headers);
        myRequest.setRequest(Request);
        myRequest.setParameter(parameter);

        Response re = RequestDataUtils.RestAssuredApi(data, myRequest);
        return re;
    }

    @Override
    public String handleOutput(Response re, HashMap<String, Object> data) {
        JsonPath jp = re.body().jsonPath();
        boolean result = true;
        String failReason = "";

        String json = re.asString();

        if ((data.get("statusCode") != null)
                && (!data.get("statusCode").toString()
                .equals(String.valueOf(re.getStatusCode())))) {
            result = result && false;
            failReason = failReason + "statusCode is expected "
                    + data.get("statusCode").toString() + " but actually "
                    + String.valueOf(re.getStatusCode()) + ". ";
        }

        if (json.length() != 0) {

            String msg = StringUtils.decodeUnicode(jp.getString("message"));
            String code = StringUtils.decodeUnicode(jp.getString("code"));

            if ((data.get("code") != null)
                    && ((jp.getString("code") == null) || (!jp.getString(
                    "code").equals(data.get("code").toString())))) {
                result = result && false;
                failReason = failReason + "code is expected "
                        + data.get("code").toString() + " but actually "
                        + jp.getString("code") + ".";
            }

            if ((data.get("msg") != null)
                    && ((msg == null) || (!msg.equals(data.get("msg").toString())))) {
                result = result && false;
                failReason = failReason + "msg is expected "
                        + data.get("msg").toString() + " but actually "
                        + jp.getString("msg") + ".";
            }

            if (data.get("custom") != null && jp.getString("data") != null) {
                String custom = data.get("custom").toString();
                String[] ArrayString = StringUtils.getArrayString(custom, ",");
                if (!StringUtils.VerificationString(jp.getString("data"), ArrayString)) {
                    result = result && false;
                    failReason = failReason + "custom is expected "
                            + data.get("custom").toString() + " but actually "
                            + jp.getString("data") + ".";
                }
            }

            if (code.equals("200")) {

            }

        }
        if (result)
            return "Pass";
        else
            return "Fail:" + failReason;
    }
}
