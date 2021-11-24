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
import com.qingzi.system.system;
import com.qingzi.testUtil.MapUtil;
import com.qingzi.testUtil.MongoDBUtil;
import com.qingzi.testUtil.RequestDataUtils;
import com.qingzi.testUtil.StringUtils;

/**
 * 
 * @ClassName:  auth   
 * @Description:TODO 获取解决方案  token（三方服务器转发客户端请求）
 * 
 * //目前访问域名直接到奔奔内部接口没有经过奇瑞外部包装（现在返回字段为id），后期会更换域名到时候接口返回字段为s_UserToken
 * @author: wff
 * @date:   2021年4月27日 下午4:03:24      
 * @Copyright:
 */
public class getToken extends QZ implements API {
	
	public String parameter; //参数集合
	public String BUid; //	用户三方唯一标识

	@Override
	public void initialize(HashMap<String, Object> data) {
		//根据邮箱查询BUid
		Document docs =  MongoDBUtil.findByid(data, "crystal", "bucUser", "username", "wufeifei@qq.com");
		BU_id = docs.getObjectId("_id").toString();
//		System.out.println(BU_id);
	}

	@Override
	public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
		parameter = MapUtil.getValue("parameter", data);
		
		BUid = MapUtil.getParameter(parameter,"BUid").trim();
		if(!BUid.equals("") && BUid.equals("code")){
			BUid = BU_id;
			parameter = parameter.replace("\"BUid\":code", "\"BUid\":\""+ BUid + "\"");
		}
		
		data.put("parameter", parameter);
		return data;
	}

	@Override
	public Response SendRequest(HashMap<String, Object> data, String Url,
			String Request) {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("appId",appId);
		headers.put("appSecret","");
		headers.put("dev","phone");
		
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
				//目前访问域名直接到奔奔内部接口没有经过奇瑞外部包装（现在返回字段为id），后期会更换域名到时候接口返回字段为s_UserToken
				s_UserToken = jp.getString("data.s_UserToken");
				System.out.println("s_UserToken = " + s_UserToken);
				userAccountId = jp.getString("data.accountId");
				MR_Id = jp.getString("data.MRId");
				
				
				/*if (data.get("CleanDB") != "" && data.get("CleanDB").equals("Y")) {
					//先查询该用户创建的个人会议
					Document doc =  MongoDBUtil.findByid(data, "crystal", "usrmgrAccount", "BUid", "feifei");
					String personalRoomId = doc.getString("personalRoomId");
//					System.out.println(personalRoomId);
					//删除个人注册后创建的个人会议室
					MongoDBUtil.deleteByid(data, "crystal", "mcmuMeetingRoom", "_id", personalRoomId);
					//删除会前注册信息
					MongoDBUtil.deleteByid(data,"crystal","usrmgrAccount","BUid","feifei");
				}*/
			}
			
		}
		if (result)
			return "Pass";
		else
			return "Fail:" + failReason;
	}
}
