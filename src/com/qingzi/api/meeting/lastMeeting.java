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

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @ClassName: lastMeeting
 * @Description //TODO  登录后获取最后一次还在进行的会议
 * @Date 下午4:42 2021/12/8
 * @Param
 * @return
 * @Author yuxiaowei
 **/

public class lastMeeting extends QZ implements API {

    public String parameter; //参数集合

    @Override
    public void initialize(HashMap<String, Object> data) {

    }

    @Override
    public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
        parameter = MapUtil.getValue("parameter", data);


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

            if (msg.equals("200")) {

                //是否是线上环境
//				if (!isProduct) {
//
//				}
				/*//接口返回meetingid
				meeting_Id = jp.getString("data.meetingId");
				m_Id = jp.getString("data.mId");
				sdk_AccountId = jp.getString("data.sdkAccountId");
				sdk_RoomId = jp.getString("data.sdkRoomId");*/
                //参会人userAccountId
                sdkAccountIdByOther = jp.getString("data.mediaInfo.sdkAccountId");
                sdkRoomId = jp.getString("data.mediaInfo.sdkRoomId");

                if (data.get("CleanDB") != "" && data.get("CleanDB").equals("Y")) {

                    //先查询该用户创建的个人会议
                    Document doc = MongoDBUtil.findByid(data, "crystal", "usrmgrAccount", "BUid", BU_id);
                    String personalRoomId = doc.getString("personalRoomId");
//					System.out.println(personalRoomId);
                    //删除企业
                    MongoDBUtil.deleteByid(data, "crystal", "usrmgrEnterprise", "name", enterprise_name);
                    //删除个人注册后创建的个人会议室
                    MongoDBUtil.deleteByid(data, "crystal", "mcmuMeetingRoom", "_id", personalRoomId);
                    //删除会前注册信息
                    MongoDBUtil.deleteByid(data, "crystal", "usrmgrAccount", "BUid", BU_id);
                    //删除会议记录
//      MongoDBUtil.deleteByid(data, "crystal", "mtmgrMeetingAuthLog", "meetingId", meetingId);
                    //删除参会表
                    MongoDBUtil.deleteByid(data, "crystal", "mtmgrMeetingParticipant", "accountId", userAccountId);
                    //删除新建会议
                    MongoDBUtil.deleteByid(data, "crystal", "mtmgrMetting", "title", title_meeting);
                }
            }

        }
        if (result)
            return "Pass";
        else
            return "Fail:" + failReason;
    }
}
