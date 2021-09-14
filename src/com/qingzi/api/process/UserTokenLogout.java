package com.qingzi.api.process;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;

import com.qingzi.interfaces.API;
import com.qingzi.process.QZ;
import com.qingzi.system.MyRequest;
import com.qingzi.testUtil.MapUtil;
import com.qingzi.testUtil.RequestDataUtils;
import com.qingzi.testUtil.StringUtils;

public class UserTokenLogout extends QZ implements API {
	
	public String parameter;  //参数集合
	
	public String app_id; //开发者在Crystal后台创建的App对应AppID	
	public String app_secret; //开发者在Crystal后台创建的App对应AppSecret	
	public String dev;  //设备类型	
	public String uid;  //用户系统用户id	

	@Override
	public void initialize(HashMap<String, Object> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
		parameter = MapUtil.getValue("parameter", data);
		
		app_id = MapUtil.getParameter(parameter, "app_id").trim();
		app_secret = MapUtil.getParameter(parameter, "app_secret").trim();
		dev = MapUtil.getParameter(parameter, "dev").trim();
		uid = MapUtil.getParameter(parameter, "uid").trim();
		
		if(!app_id.equals("") && app_id.equals("code")){
			app_id = App_id;
			parameter = parameter.replace("\"app_id\":code", "\"app_id\":\""+ app_id + "\"");
		}
		if(!app_secret.equals("") && app_secret.equals("code")){
			app_secret = App_secret;
			parameter = parameter.replace("\"app_secret\":code", "\"app_secret\":\""+ app_secret + "\"");
		}
		if(!dev.equals("") && dev.equals("code")){
			dev = "1";
			parameter = parameter.replace("\"dev\":code", "\"dev\":\""+ dev + "\"");
		}
		if(!uid.equals("") && uid.equals("code")){
			uid = "654321";
			parameter = parameter.replace("\"uid\":code", "\"uid\":\""+ uid + "\"");
		}
		data.put("parameter", parameter);
		return data;
	}

	@Override
	public Response SendRequest(HashMap<String, Object> data, String Url,
			String Request) {
//		HashMap<String, String> cookies = new HashMap<String, String>();
//		cookies.put("laravel_session",QZ.laravel_session);
		
		
		MyRequest myRequest = new MyRequest();
		myRequest.setUrl(Url);
//		myRequest.setCookies(cookies);
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
			
			if(msg.equals("OK")){
				
				//是否是线上环境
//				if (!isProduct) {
//					
//				}
			}
			
			
		}
		if (result)
			return "Pass";
		else
			return "Fail:" + failReason;
	}

}
