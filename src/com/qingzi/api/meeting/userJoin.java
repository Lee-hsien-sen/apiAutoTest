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
 *
 * @ClassName:  user_join
 * @Description:TODO  IM会议群组增加/进入用户
 * @author: WHT
 * @date:   2022年04月01日12:06:024
 * @Copyright:
 */
public class userJoin extends QZ implements API {

	public String parameter; //参数集合
	public String group_id; //群组ID
	public String user_id; //用户ID

	@Override
	public void initialize(HashMap<String, Object> data) {

	}

	@Override
	public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
		parameter = MapUtil.getValue("parameter", data);
		group_id = MapUtil.getParameter(parameter,"group_id").trim();
		user_id = MapUtil.getParameter(parameter,"user_id").trim();
		if(!group_id.equals("") && group_id.equals("code")){
			group_id = groupId;
			parameter = parameter.replace("\"group_id\":code", "\"group_id\":\""+ group_id + "\"");
		}
		if(!user_id.equals("") && user_id.equals("code")){
			user_id =userAccountId;
			parameter = parameter.replace("\"user_id\":code", "\"user_id\":\""+ userAccountId + "\"");
		}

		data.put("parameter", parameter);
		return data;
	}

	@Override
	public Response SendRequest(HashMap<String, String> headers,HashMap<String, Object> data, String Url,
			String Request) {
		MyRequest myRequest = new MyRequest();
		myRequest.setUrl("/cstcapi/cst/im/meeting/user_join");
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

			if(msg.equals("SUCCESS")){

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
