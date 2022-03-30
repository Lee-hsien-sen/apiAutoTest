package com.qingzi.api.meeting;

import com.qingzi.interfaces.API;
import com.qingzi.process.QZ;
import com.qingzi.system.MyRequest;
import com.qingzi.testUtil.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.bson.Document;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * @ClassName: createMeeting
 * @Description: 创建会议   及时会议，预约会议
 * @author: wff
 * @date: 2022年2月28日17:51:35
 * @Copyright:
 */
public class newCreateMeeting extends QZ implements API {

    public String parameter; //参数集合
    public String enterpriseId; //企业id
    public String vmrId; //会议室id
    public String host; //host信息
    public String meetingManage; //默认值见属性
    public String title; //会议标题
    public String startTime; //开始时间
    public String duration; //会议时长（秒）
    public String nickName; //邀请人昵称
    public String invitees; //邀请人
    public String creator; //创建者的userId

    @Override
    public void initialize(HashMap<String, Object> data) {

    }

    @Override
    public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
        parameter = MapUtil.getValue("parameter", data);

        enterpriseId = MapUtil.getParameter(parameter, "enterpriseId").trim();
        vmrId = MapUtil.getParameter(parameter, "vmrId").trim();
        host = MapUtil.getParameter(parameter, "host").trim();
        meetingManage = MapUtil.getParameter(parameter, "meetingManage").trim();
        title = MapUtil.getParameter(parameter, "title").trim();
        startTime = MapUtil.getParameter(parameter, "startTime").trim();
        duration = MapUtil.getParameter(parameter, "duration").trim();
        nickName = MapUtil.getParameter(parameter, "nickName").trim();
        invitees = MapUtil.getParameter(parameter, "invitees").trim();
        creator = MapUtil.getParameter(parameter, "creator").trim();
        if (!enterpriseId.equals("") && enterpriseId.equals("code")) {
            enterpriseId = enterprise_Id;
            parameter = parameter.replace("\"enterpriseId\":code", "\"enterpriseId\":\"" + enterpriseId + "\"");
        }
        if (!vmrId.equals("") && vmrId.equals("code")) {
            //固定会议室
            //MRId = MR_Id;
            //临时会议
            vmrId = null;
            parameter = parameter.replace("\"vmrId\":code", "\"vmrId\":" + vmrId + "");
        }
        if (!host.equals("") && host.equals("code")) {
            host = "";
            parameter = parameter.replace("\"host\":code", "\"host\":\"" + host + "\"");
        }
        if (!meetingManage.equals("") && meetingManage.equals("code")) {
            meetingManage = "";
            parameter = parameter.replace("\"meetingManage\":code", "\"meetingManage\":\"" + meetingManage + "\"");
        }
        if (!title.equals("") && title.equals("code")) {
            title = title_meeting;
            parameter = parameter.replace("\"title\":code", "\"title\":\"" + title + "\"");
        }
        if (!startTime.equals("") && startTime.equals("code")) {
            startTime = String.valueOf(System.currentTimeMillis());
            parameter = parameter.replace("\"startTime\":code", "\"startTime\":" + startTime + "");
        }
        if (!duration.equals("") && duration.equals("code")) {
            duration = String.valueOf(172800);
            parameter = parameter.replace("\"duration\":code", "\"duration\":" + duration + "");
        }
        if (!nickName.equals("") && nickName.equals("code")) {
            nickName = hostNickName;
            parameter = parameter.replace("\"nickName\":code", "\"nickName\":\"" + nickName + "\"");
        }
        if (!invitees.equals("") && invitees.equals("code")) {
            invitees = "";
            parameter = parameter.replace("\"invitees\":code", "\"invitees\":" + invitees + "");
        }
        if (!creator.equals("") && creator.equals("code")) {
            creator = userAccountId;
            parameter = parameter.replace("\"creator\":code", "\"creator\":\"" + creator + "\"");
        }

        data.put("parameter", parameter);
        return data;
    }

    @Override
    public Response SendRequest(HashMap<String, String> headers,HashMap<String, Object> data, String Url,
                                String Request) {
        MyRequest myRequest = new MyRequest();
        myRequest.setUrl("/cstcapi/moms/mtmgr/v1/admin/createMeeting");
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

                //是否是线上环境
//				if (!isProduct) {
//
//				}
                //接口返回meetingid
                meeting_Id = jp.getString("data.meetingId");
                m_Id = jp.getString("data.mId");
                userId = jp.getString("data.host.userId");
                pwd_meeting = jp.getString("data.password");
//                sdk_RoomId = jp.getString("data.sdkRoomId");

                //查询新建会议的MRId
//                Document docs = MongoDBUtil.findByid(data, "crystal", "mtmgrMetting", "enterpriseId", enterpriseId);
//                String meetingId = docs.getString("_id");
//                //mid
//                mId_meeting = docs.getString("mId");
//                //pwd
//                pwd_meeting = docs.getString("pwd");
//                //MRId
//                MR_Id = docs.getString("MRId");
//                System.out.println(meetingId);
            }

        }
        if (result)
            return "Pass";
        else
            return "Fail:" + failReason;
    }
}
