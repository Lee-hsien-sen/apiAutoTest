package com.qingzi.api.meeting;

import com.qingzi.interfaces.API;
import com.qingzi.process.QZ;
import com.qingzi.system.MyRequest;
import com.qingzi.testUtil.MapUtil;
import com.qingzi.testUtil.MongoDBUtil;
import com.qingzi.testUtil.RequestDataUtils;
import com.qingzi.testUtil.StringUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.sf.json.JSONObject;
import org.bson.Document;

import java.util.HashMap;

/**
 * @ClassName: startRecord
 * @Description: 个人打开录制
 * @author: xiaoweiyu
 * @date: 5/6/22 11:20 AM
 * @Copyright:
 */

public class startRecord extends QZ implements API {

    public String parameter; //参数集合
    public String meetingId; //会议全局唯一id，和mId 必传一个(推荐使用)


    @Override
    public void initialize(HashMap<String, Object> data) {

    }

    @Override
    public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
        parameter = MapUtil.getValue("parameter", data);

        meetingId = MapUtil.getParameter(parameter, "meetingId").trim();
        if (!meetingId.equals("") && meetingId.equals("code")) {
            meetingId = meeting_Id;
            parameter = parameter.replace("\"meetingId\":code", "\"meetingId\":\"" + meetingId + "\"");
        }

        data.put("parameter", parameter);
        return data;
    }

    @Override
    public Response SendRequest(HashMap<String, String> headers, HashMap<String, Object> data, String Url,
                                String Request) {
        MyRequest myRequest = new MyRequest();
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
