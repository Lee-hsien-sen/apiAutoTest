package com.qingzi.api.meeting;

import com.qingzi.interfaces.API;
import com.qingzi.process.QZ;
import com.qingzi.system.MyRequest;
import com.qingzi.testUtil.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.codec.DecoderException;

import java.util.HashMap;


public class exchangeByOther extends QZ implements API {
    public String parameter; //参数集合
    public String pub;


    @Override
    public void initialize(HashMap<String, Object> data) {

    }

    @Override
    public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
        parameter = MapUtil.getValue("parameter", data);

        pub = MapUtil.getParameter(parameter, "pub").trim();

        if (!pub.equals("") && pub.equals("code")) {
//            HashMap<String, String> pubMap = new HashMap<String, String>();
//            pubMap.put("pub", clientPub);
//            System.out.println("operated::" + JSONObject.fromObject(pubMap));
            pub = clientPubByOther;
            parameter = parameter.replace("\"pub\":code", "\"pub\":\"" + pub + "\"");
//            Log.logInfo("==parameter:"+parameter);
        }
        data.put("parameter", parameter);
        return data;
    }

    @Override
    public Response SendRequest(HashMap<String, String> headers, HashMap<String, Object> data, String Url,
                                String Request) {
        MyRequest myRequest = new MyRequest();
        myRequest.setUrl("/cstcapi/key/exchange");
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Cst-Token", s_UserTokenByOther);
        myRequest.setHeaders(header);
        myRequest.setRequest(Request);
        myRequest.setParameter(parameter);
        Log.logInfo("==header:"+ header+ "==parameter:"+parameter);

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

//				*//接口返回token
                serverPubByOther = jp.getString("data.pub");
                try {
                    authKeyByOther = EccUtils.doExchange(userAccountIdByOther, dev, serverPubByOther,clientPriByOther);
                } catch (DecoderException e) {
                    e.printStackTrace();
                    throw new RuntimeException("密钥生成失败");
                }
            }

        }
        if (result)
            return "Pass";
        else
            return "Fail:" + failReason;
    }
}
