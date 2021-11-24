package com.qingzi.api.meeting;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Random;

import org.bson.Document;

import com.qingzi.interfaces.API;
import com.qingzi.process.QZ;
import com.qingzi.system.MyRequest;
import com.qingzi.testUtil.MapUtil;
import com.qingzi.testUtil.MongoDBUtil;
import com.qingzi.testUtil.RequestDataUtils;
import com.qingzi.testUtil.StringUtils;

/**
 * 
 * @ClassName:  register   
 * @Description:TODO   注册企业-为会议接口做前置准备，不做测试（没有删除删除接口也没给数据库，单独测试注册企业会有大量脏数据）
 * @author: wff
 * @date:   2021年4月6日 下午5:41:40      
 * @Copyright:
 */
public class register extends QZ implements API {
	
	public String parameter; //参数集合
	public String name;  //企业名称
	public String describe; //企业描述

	@Override
	public void initialize(HashMap<String, Object> data) {
		
	}

	@Override
	public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
		parameter = MapUtil.getValue("parameter", data);
		
		name = MapUtil.getParameter(parameter,"name").trim();
		describe = MapUtil.getParameter(parameter,"describe").trim();
		int random = new Random().nextInt(9000)+ 1000;
		
		if(!name.equals("") && name.equals("code")){
			name = enterprise_name;
			parameter = parameter.replace("\"name\":code", "\"name\":\""+ name + "\"");
		}
		if(!describe.equals("") && describe.equals("code")){
			describe = "测试企业-ff" + random; 
			parameter = parameter.replace("\"describe\":code", "\"describe\":\""+ describe + "\"");
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
				//企业id  目前只获取一次
				enterprise_Id = jp.getString("data.id");
				if (data.get("CleanDB") != "" && data.get("CleanDB").equals("Y")) {
					//删除企业
					MongoDBUtil.deleteByid(data, "crystal", "usrmgrEnterprise", "name", enterprise_name);
				}
//				System.out.println(enterprise_Id);
				
			}
			
		}
		if (result)
			return "Pass";
		else
			return "Fail:" + failReason;
	}
}
