package com.qingzi.api.meeting;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import net.sf.json.JSONObject;

import org.bson.Document;

import com.fasterxml.jackson.core.sym.Name;
import com.qingzi.interfaces.API;
import com.qingzi.process.QZ;
import com.qingzi.system.MyRequest;
import com.qingzi.testUtil.MapUtil;
import com.qingzi.testUtil.MongoDBUtil;
import com.qingzi.testUtil.RequestDataUtils;
import com.qingzi.testUtil.StringUtils;

/**
 *
 * @ClassName:  checkParticipant
 * @Description:TODO校验成员是否在会议中
 * @author: wff
 * @date:   2021年5月14日 下午4:08:26
 * @Copyright:
 */
public class checkParticipant extends QZ implements API {

	public String parameter; //参数集合
	public String mId; //会议短Id
	public String users; //需要校验的用户列表

	@Override
	public void initialize(HashMap<String, Object> data) {

	}

	@Override
	public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
		parameter = MapUtil.getValue("parameter", data);



		mId = MapUtil.getParameter(parameter,"mId").trim();
		users = MapUtil.getParameter(parameter,"users").trim();
		if(!mId.equals("") && mId.equals("code")){
			mId = m_Id;
			parameter = parameter.replace("\"mId\":code", "\"mId\":\""+ mId + "\"");
		}
		if(!users.equals("") && users.equals("code")){
			HashMap<String, String> userMap = new HashMap<String, String>();
			userMap.put("dev", "1");
			userMap.put("userAccountId", userAccountId);
			ArrayList<Object> userList = new ArrayList<Object>();
			userList.add(JSONObject.fromObject(userMap));
			parameter = parameter.replace("\"users\":code", "\"users\":"+ userList + "");
		}

		data.put("parameter", parameter);
		return data;
	}

	@Override
	public Response SendRequest(HashMap<String, String> headers,HashMap<String, Object> data, String Url,
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

			String msg=StringUtils.decodeUnicode(jp.getString("message"));

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

			if(msg.equals("success")){

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
				Document docs =  MongoDBUtil.findByid(data, "crystal", "mtmgrMetting", "title", title_meeting);
				String meetingId = docs.getString("_id");
				//mid
				mId_meeting = docs.getString("mId");
				//pwd
				pwd_meeting = docs.getString("pwd");
				System.out.println(meetingId);
			}

		}
		if (result)
			return "Pass";
		else
			return "Fail:" + failReason;
	}
}
