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
 * @Description: 音视频下行数据上报
 * @author: wff
 * @date: 2022年4月24日16:06:04
 * @Copyright:
 */
public class dataDown extends QZ implements API {

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
                    "    \"headerInfo\":{\n" +
                    "            \"dataSchema\":\"downstream_info\",\n" +
                    "            \"eventId\":1,\n" +
                    "            \"eventName\":\"downstream_report\",\n" +
                    "            \"logTime\":\"1231231231\",\n" +
                    "            \"roomId\":\"1231231\",\n" +
                    "            \"peerId\":\"1231\",\n" +
                    "            \"dataSource\": \"demo-ff\", \n" +
                    "            \"sessionId\":\"0ccbaa01-1629300267175\"\n" +
                    " \n" +
                    "    },\n" +
                    "    \"downBodyInfo\":{\n" +
                    "      \"userId\":\"12312\",\n" +
                    "      \"deviceType\":\"1\",\n" +
                    "      \"deviceVersion\":\"1.1.1\",\n" +
                    "      \"networkType\":3, \n" +
                    "      \"device\":\"ios\",\n" +
                    "      \"sdkVersion\":\"1231\",\n" +
                    "      \"clientCpuRate\":\"0.1\",\n" +
                    "      \"systemCpuRate\":\"0.2\",\n" +
                    "      \"systemCpuFrequencyRate\":\"0.3\",\n" +
                    "      \"roomId\":\"123\",\n" +
                    "      \"bitrateRecv\":\"1.2\",\n" +
                    "      \"bytesRecv\":\"222\",\n" +
                    "      \"packetsRecv\":\"3333\",\n" +
                    "      \"fractionLost\":\"0.1\",\n" +
                    "      \"roundTripTime\":\"2.3\",\n" +
                    "      \"audioInfos\":[\n" +
                    "        {\n" +
                    "           \"subscribeUserId\":\"12313\",\n" +
                    "           \"produceId\":\"123132\",\n" +
                    "           \"kind\":\"mic\",\n" +
                    "            \"ssrc\":\"123214\",\n" +
                    "            \"packetsReceived\":1,\n" +
                    "            \"fecPacketsReceived\":233,\n" +
                    "            \"fecPacketsDiscarded\":233,\n" +
                    "            \"bytesReceived\":122,\n" +
                    "            \"bitrateRecv\":\"1.2\",\n" +
                    "            \"fractionLost\":\"0.1\",\n" +
                    "            \"delayEstimateMs\":\"1.3\",\n" +
                    "            \"jitter\": 1.2,\n" +
                    "            \"packetsLost\":100,\n" +
                    "            \"jitterBufferEmittedCount\":123,\n" +
                    "            \"totalSamplesReceived\":123,\n" +
                    "            \"concealedSamples\":123,\n" +
                    "            \"silentConcealedSamples\":123,\n" +
                    "            \"insertedSamplesForDeceleration\":123,\n" +
                    "            \"removedSamplesForAcceleration\":12,\n" +
                    "            \"jitter\":1.2,\n" +
                    "            \"jitterBufferDelay\":1.2,\n" +
                    "            \"packetsLost\":12.2,\n" +
                    "            \"delayEstimateMs\":1.2,\n" +
                    "            \"audioVolume\":1.2,\n" +
                    "             \"audioLevel\":1,\n" +
                    "            \"bitrateRecv\":1.2,\n" +
                    "             \"fractionLost\":0.1,\n" +
                    "             \"roundTripTime\":2.3,\n" +
                    "            \"audioCantons\":1.2,\n" +
                    "            \"audioDelay\":1.2,\n" +
                    "           \"consumeId\":\"12312\",\n" +
                    "            \"audioCodec\":{\n" +
                    "                    \"payloadType\":1,\n" +
                    "                    \"mimeType\":\"opus\",\n" +
                    "                    \"clockRate\":1,\n" +
                    "                    \"channels\":1\n" +
                    "            }\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"videoInfos\":[\n" +
                    "        {\n" +
                    "          \"subscribeUserId\":12313,\n" +
                    "          \"produceId\":123132,\n" +
                    "          \"kind\":\"webcam\",\n" +
                    "           \"ssrc\":123214, \n" +
                    "            \"bitrateRecv\":1.2,\n" +
                    "            \"roundTripTime\":123,\n" +
                    "            \"fractionLost\":0.1,\n" +
                    "            \"jitter\": 1.2,\n" +
                    "            \"packetsLost\":100,\n" +
                    "            \"packetsReceived\":100,\n" +
                    "            \"bytesReceived\":123,\n" +
                    "            \"framesReceived\":123,\n" +
                    "            \"frameWidth\":12,\n" +
                    "            \"frameHeight\":13,\n" +
                    "            \"framesPerSecond\":123,\n" +
                    "            \"framesDecoded\":12,\n" +
                    "            \"keyFramesDecoded\":12,\n" +
                    "            \"framesDropped\":12,\n" +
                    "            \"totalDecodeTime\":1.2,\n" +
                    "            \"totalInterFrameDelay\":1.2,\n" +
                    "            \"totalSquaredInterFrameDelay\":1.2,\n" +
                    "            \"firCount\":12,\n" +
                    "            \"pliCount\":13,\n" +
                    "            \"nackCount\":12,\n" +
                    "            \"qpSum\":12 ,\n" +
                    "            \"qp\":1.2,\n" +
                    "            \"renderDelayMs\":12,\n" +
                    "            \"targetDelayMs\":1,\n" +
                    "            \"videoCatonMs\":1.2,\n" +
                    "            \"jitterBufferDelay\":1.2,\n" +
                    "            \"freezeCount\":100,\n" +
                    "            \"totalFreezesDuration\":100.23,\n" +
                    "            \"totalPausesDuration\":12.3,\n" +
                    "            \"totalFramesDuration\":12.3,\n" +
                    "            \"sumSquaredFrameDurations\":15.3,\n" +
                    "            \"totalCatonCount\":12.3,\n" +
                    "            \"totalCatonDelayMs\":12.3,\n" +
                    "            \"consumeId\":\"12312\",\n" +
                    "            \"videoCodec\":{\n" +
                    "                    \"payloadType\":1,\n" +
                    "                    \"mimeType\":\"opus\",\n" +
                    "                    \"clockRate\":1,\n" +
                    "                    \"channels\":1\n" +
                    "            }\n" +
                    "        }\n" +
                    "      ]\n" +
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
        myRequest.setUrl("/cstcapi/cst/data/clientlog/dataUpload");
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
