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
 * @ClassName:  editName
 * @Description:关闭视频
 * @author: wangshushu
 * @date:   2021年9月28日17:31:27
 * @Copyright:
 */
public class createBroad extends QZ implements API {

	public String parameter; //参数集合
	public String roomId; //会议Id
	public String peerId; //用户的Id

	@Override
	public void initialize(HashMap<String, Object> data) {

	}

	@Override
	public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
		parameter = MapUtil.getValue("parameter", data);
        roomId = MapUtil.getParameter(parameter,"roomId").trim();
        peerId = MapUtil.getParameter(parameter,"peerId").trim();
		if(!roomId.equals("") && roomId.equals("code")){
            roomId = meeting_Id;
			parameter = parameter.replace("\"roomId\":code", "\"roomId\":\""+ roomId + "\"");
		}
        if(!peerId.equals("") && peerId.equals("code")){
            peerId = userId;
            parameter = parameter.replace("\"peerId\":code", "\"peerId\":\""+ peerId + "\"");
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

				board_Id = jp.getString("data.boardId");
				System.out.println("boardId = " + board_Id);
				/*//接口返回meetingid
				meeting_Id = jp.getString("data.meetingId");
				m_Id = jp.getString("data.mId");
				sdk_AccountId = jp.getString("data.sdkAccountId");
				sdk_RoomId = jp.getString("data.sdkRoomId");*/

				//查询新建会议的MRId
//				Document docs =  MongoDBUtil.findByid(data, "crystal", "mtmgrMetting", "title", title_meeting);
//				String meetingId = docs.getString("_id");
//				//mid
//				mId_meeting = docs.getString("mId");
//				//pwd
//				pwd_meeting = docs.getString("pwd");
//				System.out.println(meetingId);
			}

		}
		if (result)
			return "Pass";
		else
			return "Fail:" + failReason;
	}
}
