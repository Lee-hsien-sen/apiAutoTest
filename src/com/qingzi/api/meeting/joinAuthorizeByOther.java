package com.qingzi.api.meeting;

import com.qingzi.interfaces.API;
import com.qingzi.process.QZ;
import com.qingzi.system.MyRequest;
import com.qingzi.testUtil.Log;
import com.qingzi.testUtil.MapUtil;
import com.qingzi.testUtil.RequestDataUtils;
import com.qingzi.testUtil.StringUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;

/**
 * @ClassName: joinAuthorize
 * @Description:获取会中授权（三方服务端调用） 参会人使用
 * @author: wff
 * @date: 2022年3月29日11:00:26
 * @Copyright:
 */
public class joinAuthorizeByOther extends QZ implements API {

    public String parameter; //参数集合
    public String meetingId; //会议全局唯一id，和mId 必传一个(推荐使用)
    public String mId; //会议短Id，和meetingId 必传一个
    public String userId; //crystal的用户id


    @Override
    public void initialize(HashMap<String, Object> data) {

    }

    @Override
    public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
        parameter = MapUtil.getValue("parameter", data);

        meetingId = MapUtil.getParameter(parameter, "meetingId").trim();
        mId = MapUtil.getParameter(parameter, "mId").trim();
        userId = MapUtil.getParameter(parameter, "userId").trim();
        if (!meetingId.equals("") && meetingId.equals("code")) {
            meetingId = meeting_Id;
            parameter = parameter.replace("\"meetingId\":code", "\"meetingId\":\"" + meetingId + "\"");
        }
        if (!mId.equals("") && mId.equals("code")) {
            mId = m_Id;
            parameter = parameter.replace("\"mId\":code", "\"mId\":\"" + mId + "\"");
        }
        if (!userId.equals("") && userId.equals("code")) {
            userId = userAccountIdByOther;
            parameter = parameter.replace("\"userId\":code", "\"userId\":\"" + userId + "\"");
        }

        data.put("parameter", parameter);
        return data;
    }

    @Override
    public Response SendRequest(HashMap<String, String> headers, HashMap<String, Object> data, String Url,
                                String Request) {
        MyRequest myRequest = new MyRequest();
//		myRequest.setUrl(Url + "?userAccountId="+ userAccountId);
        myRequest.setUrl("/cstcapi/moms/mtmgr/v1/admin/joinAuthorize");
        myRequest.setHeaders(headers);
        myRequest.setRequest(Request);
        myRequest.setParameter(parameter);
        Log.logInfo("==header:"+ headers+ "==parameter:"+parameter);

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
