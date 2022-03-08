package com.qingzi.api.meeting;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Random;

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
 * @ClassName:  createMeeting   
 * @Description:TODO 创建会议   该接口后期也得修改，userAccountId需要从SUserToken中解析（需要调用奇瑞外层域名）
 * @author: wff
 * @date:   2021年4月28日 下午5:24:29      
 * @Copyright:
 */
public class createMeeting extends QZ implements API {
	
	public String parameter; //参数集合
	public String enterpriseId; //企业id
	public String title; //会议标题
	public String startTime; //头像
	public String endTime; //头像

	@Override
	public void initialize(HashMap<String, Object> data) {

	}

	@Override
	public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
		parameter = MapUtil.getValue("parameter", data);
		
		enterpriseId = MapUtil.getParameter(parameter,"enterpriseId").trim();
		title = MapUtil.getParameter(parameter,"title").trim();
		startTime = MapUtil.getParameter(parameter,"startTime").trim();
		endTime = MapUtil.getParameter(parameter,"endTime").trim();
		if(!enterpriseId.equals("") && enterpriseId.equals("code")){
			enterpriseId = enterprise_Id; 
			parameter = parameter.replace("\"enterpriseId\":code", "\"enterpriseId\":\""+ enterpriseId + "\"");
		}
		if(!title.equals("") && title.equals("code")){
			title = title_meeting; 
			parameter = parameter.replace("\"title\":code", "\"title\":\""+ title + "\"");
		}
		if(!startTime.equals("") && startTime.equals("code")){
			startTime = String.valueOf(System.currentTimeMillis());
			parameter = parameter.replace("\"startTime\":code", "\"startTime\":"+ startTime + "");
		}
		if(!endTime.equals("") && endTime.equals("code")){
			endTime = String.valueOf(86400000);
			parameter = parameter.replace("\"endTime\":code", "\"endTime\":"+ endTime + "");
		}
		
		data.put("parameter", parameter);
		return data;
	}

	@Override
	public Response SendRequest(HashMap<String, Object> data, String Url,
			String Request) {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("SUserToken",s_UserToken);
		headers.put("appId",appId);
		headers.put("dev",dev);
		System.out.println("s_UserToken:  "+s_UserToken+"appId:   "+appId+"dev:   "+dev);
		
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
				meeting_Id = jp.getString("data.meetingId");
				m_Id = jp.getString("data.mId");
				pwd_meeting = jp.getString("data.password");
				userId = jp.getString("data.host.userId");
				sdk_RoomId = jp.getString("data.sdkRoomId");
				
//				//查询新建会议的MRId
//				Document docs =  MongoDBUtil.findByid(data, "crystal", "mtmgrMetting", "title", title);
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
