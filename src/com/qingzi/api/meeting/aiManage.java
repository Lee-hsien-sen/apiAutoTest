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
 * @ClassName: aiManage
 * @Description:TODO 主持人开关AI字幕
 * @author: wangshujie
 * @date: 2021/10/15 下午5:07
 * @Copyright:
 */

public class aiManage extends QZ implements API {

    public String parameter; //参数集合
    public String meetingId; //解决方案会议室Id
    public String asrSwitch; //语音识别开关
    public String translateSwitch; //翻译开关
    public String allow; //是否允许参会人请求打开


    @Override
    public void initialize(HashMap<String, Object> data) {

    }

    @Override
    public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
        parameter = MapUtil.getValue("parameter", data);


        meetingId = MapUtil.getParameter(parameter,"meetingId").trim();
        asrSwitch = MapUtil.getParameter(parameter,"asrSwitch").trim();
        translateSwitch = MapUtil.getParameter(parameter,"translateSwitch").trim();
        allow = MapUtil.getParameter(parameter,"allow").trim();

        if(!meetingId.equals("") && meetingId.equals("code")){
            meetingId = meeting_Id;
            parameter = parameter.replace("\"meetingId\":code", "\"meetingId\":\""+ meetingId + "\"");
        }
        if(!asrSwitch.equals("")&&asrSwitch.equals("code")){
            asrSwitch = asrSwitch;
            parameter = parameter.replace("\"asrSwitch\":code","\"asrSwitch\":\""+ asrSwitch + "\"");
        }
        if(!translateSwitch.equals("")&&translateSwitch.equals("code")){
            translateSwitch = translateSwitch;
            parameter = parameter.replace("\"translateSwitch\":code","\"translateSwitch\":\""+ translateSwitch + "\"");
        }
        if(!allow.equals("")&&allow.equals("code")){
            allow = allow;
            parameter = parameter.replace("\"allow\":code","\"allow\":\""+ allow + "\"");
        }


        data.put("parameter", parameter);
        return data;
    }

    @Override
    public Response SendRequest(HashMap<String, String> headers,HashMap<String, Object> data, String Url, String Request) {
        MyRequest myRequest = new MyRequest();
        myRequest.setUrl(Url);
        myRequest.setHeaders(headers);
        myRequest.setRequest(Request);
        myRequest.setParameter(parameter);
        Log.logInfo("Headers="+ headers + "appkey:"+authKey);

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
            String code= StringUtils.decodeUnicode(jp.getString("code"));


            if ((data.get("code") != null )
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

            if(data.get("custom") != null && jp.getString("data")!=null){
                String custom=data.get("custom").toString();
                String[] ArrayString=StringUtils.getArrayString(custom,",");
                if(!StringUtils.VerificationString(jp.getString("data"),ArrayString)){
                    result = result && false;
                    failReason = failReason + "custom is expected "
                            + data.get("custom").toString() + " but actually "
                            + jp.getString("data") + ".";
                }
            }

            if(code.equals("200")){

                //是否是线上环境
//				if (!isProduct) {
//
//				}
				/*//接口返回meetingid
				meeting_Id = jp.getString("data.meetingId");
				m_Id = jp.getString("data.mId");
				sdk_AccountId = jp.getString("data.sdkAccountId");
				sdk_RoomId = jp.getString("data.sdkRoomId");*/

                //查询新建会议的MRId
//                Document docs =  MongoDBUtil.findByid(data, "crystal", "mtmgrMetting", "title", title_meeting);
//                String meetingId = docs.getString("_id");
//                //mid
//                mId_meeting = docs.getString("mId");
//                //pwd
//                pwd_meeting = docs.getString("pwd");
//                System.out.println(meetingId);
            }

        }
        if (result)
            return "Pass";
        else
            return "Fail:" + failReason;
    }
}

