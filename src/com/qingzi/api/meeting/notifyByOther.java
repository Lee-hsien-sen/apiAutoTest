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
 *
 * @ClassName:  joinSuccess
 * @Description:TODO   非正常形式入会，为测会中接口提供模拟socket入会，不做测试
 * @author: wff
 * @date:   2021年5月14日 上午11:32:02
 * @Copyright:
 */
public class notifyByOther extends QZ implements API {

	public String parameter; //参数集合
	public String ts; //时间戳
	public String roomId; //媒体房间id
	public String peerId; //媒体房间id

	@Override
	public void initialize(HashMap<String, Object> data) {

	}

	@Override
	public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
		parameter = MapUtil.getValue("parameter", data);

		ts = MapUtil.getParameter(parameter,"ts").trim();
		roomId = MapUtil.getParameter(parameter,"roomId").trim();
		peerId = MapUtil.getParameter(parameter,"peerId").trim();
		if(!ts.equals("") && ts.equals("code")){
			parameter = parameter.replace("\"ts\":code", "\"ts\":\""+ System.currentTimeMillis() + "\"");
		}
		if(!roomId.equals("") && roomId.equals("code")){
			roomId = sdkRoomId;
			parameter = parameter.replace("\"roomId\":code", "\"roomId\":\""+ roomId + "\"");
		}
		if(!peerId.equals("") && peerId.equals("code")){
			peerId = sdkAccountIdByOther;
			parameter = parameter.replace("\"peerId\":code", "\"peerId\":\""+ peerId + "\"");
		}
		data.put("parameter", parameter);
		return data;
	}

	@Override
	public Response SendRequest(HashMap<String, String> headers,HashMap<String, Object> data, String Url,
			String Request) {
		MyRequest myRequest = new MyRequest();
		myRequest.setUrl("/cstcapi/moms/mtmgr/v1/mmc/notify");
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

			String msg=StringUtils.decodeUnicode(jp.getString("message"));
			String code=StringUtils.decodeUnicode(jp.getString("code"));

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
				//接口返回meetingid
				/*meeting_Id = jp.getString("data.meetingId");
				sdk_AccountId = jp.getString("data.sdkAccountId");
				sdk_RoomId = jp.getString("data.sdkRoomId");

				//查询新建会议的MRId
				Document docs =  MongoDBUtil.findByid(data, "crystal", "mtmgrMetting", "title", title_meeting);
				String meetingId = docs.getString("_id");
				//mid
				mId_meeting = docs.getString("mId");
				//pwd
				pwd_meeting = docs.getString("pwd");
				System.out.println(meetingId);
				if (data.get("CleanDB") != "" && data.get("CleanDB").equals("Y")) {

					//先查询该用户创建的个人会议
					Document doc =  MongoDBUtil.findByid(data, "crystal", "usrmgrAccount", "BUid", "feifei");
					String personalRoomId = doc.getString("personalRoomId");
//					System.out.println(personalRoomId);
					//删除个人注册后创建的个人会议室
					MongoDBUtil.deleteByid(data, "crystal", "mcmuMeetingRoom", "_id", personalRoomId);
					//删除会前注册信息
					MongoDBUtil.deleteByid(data,"crystal","usrmgrAccount","BUid","feifei");
					//删除会议记录
					MongoDBUtil.deleteByid(data, "crystal", "mtmgrMeetingAuthLog", "meetingId", meetingId);
					//删除参会表
					MongoDBUtil.deleteByid(data, "crystal", "mtmgrMeetingParticipant", "accountId", userAccountId);
					//删除新建会议
					MongoDBUtil.deleteByid(data, "crystal", "mtmgrMetting", "title", title_meeting);
				}*/
			}

		}
		if (result)
			return "Pass";
		else
			return "Fail:" + failReason;
	}
}
