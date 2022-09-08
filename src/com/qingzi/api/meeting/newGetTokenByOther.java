package com.qingzi.api.meeting;

import com.qingzi.interfaces.API;
import com.qingzi.process.QZ;
import com.qingzi.system.MyRequest;
import com.qingzi.testUtil.EccUtils;
import com.qingzi.testUtil.MapUtil;
import com.qingzi.testUtil.RequestDataUtils;
import com.qingzi.testUtil.StringUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: newGetTokenByOther
 * @Description:TODO
 * @author: yuxiaowei
 * @date: 2022/2/15 上午11:33
 * @Copyright:
 */

public class newGetTokenByOther extends QZ implements API {
    public String parameter; //参数集合
    public String Uid;


    @Override
    public void initialize(HashMap<String, Object> data) {

    }

    @Override
    public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
        parameter = MapUtil.getValue("parameter", data);

        Uid = MapUtil.getParameter(parameter, "Uid").trim();

        if (!Uid.equals("") && Uid.equals("code")) {
            Uid = userAccountIdByOther;
            parameter = parameter.replace("\"Uid\":code", "\"Uid\":\"" + Uid + "\"");
        }


        data.put("parameter", parameter);
        return data;
    }

    @Override
    public Response SendRequest(HashMap<String, String> headers, HashMap<String, Object> data, String Url,
                                String Request) {
        MyRequest myRequest = new MyRequest();
        myRequest.setUrl("/cstcapi/tas/user/v1/GetToken");
//        myRequest.setHeaders(headers);
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

                //是否是线上环境
//				if (!isProduct) {
//
//				}
//				*//接口返回token
                s_UserTokenByOther = jp.getString("data.token");
                Map<String, String> pair = EccUtils.getPair();
                clientPubByOther = pair.get("pub");
                clientPriByOther = pair.get("pri");

                System.out.println(" s_UserTokenByOther = " +  s_UserTokenByOther);
//                userAccountId = jp.getString("data.accountId");
//                MR_Id = jp.getString("data.MRId");

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
