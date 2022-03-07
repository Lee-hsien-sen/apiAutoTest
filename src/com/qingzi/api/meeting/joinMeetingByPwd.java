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
 * @ClassName:  joinMeetingByPwd   
 * @Description:TODO  通过会议室Id和密码进入会议  该接口后期也得修改，userAccountId需要从SUserToken中解析（需要调用奇瑞外层域名）
 * @author: wff
 * @date:   2021年4月30日 上午10:50:42      
 * @Copyright:
 */
public class joinMeetingByPwd extends QZ implements API {
	
	public String parameter; //参数集合
	public String enterpriseId; //企业id
	public String nickName; //昵称
	public String avatarUrl; //头像
	public String mId; //会议短Id
	public String pwd; //来宾密码或主持人密码

	@Override
	public void initialize(HashMap<String, Object> data) {

	}

	@Override
	public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
		parameter = MapUtil.getValue("parameter", data);
		
		enterpriseId = MapUtil.getParameter(parameter,"enterpriseId").trim();
		avatarUrl = MapUtil.getParameter(parameter,"avatarUrl").trim();
		nickName = MapUtil.getParameter(parameter,"nickName").trim();
		mId = MapUtil.getParameter(parameter,"mId").trim();
		pwd = MapUtil.getParameter(parameter,"pwd").trim();
		if(!enterpriseId.equals("") && enterpriseId.equals("code")){
			enterpriseId = enterprise_Id; 
			parameter = parameter.replace("\"enterpriseId\":code", "\"enterpriseId\":\""+ enterpriseId + "\"");
		}
		if(!avatarUrl.equals("") && avatarUrl.equals("code")){
			avatarUrl = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3101694723,748884042&fm=26&gp=0.jpg"; 
			parameter = parameter.replace("\"avatarUrl\":code", "\"avatarUrl\":\""+ avatarUrl + "\"");
		}
		if(!nickName.equals("") && nickName.equals("code")){
			nickName = "昵称-ff"; 
			parameter = parameter.replace("\"nickName\":code", "\"nickName\":\""+ nickName + "\"");
		}
		if(!mId.equals("") && mId.equals("code")){
			mId = m_Id;
			parameter = parameter.replace("\"mId\":code", "\"mId\":\""+ mId + "\"");
		}
		if(!pwd.equals("") && pwd.equals("code")){
			pwd = pwd_meeting;
			parameter = parameter.replace("\"pwd\":code", "\"password\":\""+ pwd + "\"");
		}
		
		data.put("parameter", parameter);
		return data;
	}

	@Override
	public Response SendRequest(HashMap<String, Object> data, String Url,
			String Request) {
		HashMap<String, String> headers = new HashMap<String, String>();
		//需要调用奇瑞域名才能获取
		headers.put("SUserToken",s_UserToken);
		headers.put("appId",appId);
		headers.put("dev",dev);
		
		MyRequest myRequest = new MyRequest();
//		myRequest.setUrl(Url + "?userAccountId="+ userAccountId);
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
//				//查询新建会议的MRId
//				Document docs =  MongoDBUtil.findByid(data, "crystal", "mtmgrMetting", "title", title_meeting);
//				String meetingId = docs.getString("_id");
//				//mid
//				mId = docs.getString("mId");
//				//pwd
//				pwd = docs.getString("password");
//				//mediaInfo  参会人Accountid
//				sdkAccountId = jp.getString("data.mediaInfo.sdkAccountId");
//				//mediaInfo 媒体房间id
//				sdkRoomId = jp.getString("data.mediaInfo.sdkRoomId");
//				System.out.println(meetingId);
			}
			
		}
		if (result)
			return "Pass";
		else
			return "Fail:" + failReason;
	}
}
