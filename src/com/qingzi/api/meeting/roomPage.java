package com.qingzi.api.meeting;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Random;

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
 * @ClassName:  roomPage   (一期不用)
 * @Description:TODO 获取会议室列表------后期可能还得改，目前header中的SUserToken还未传，得等奇瑞外层包装后
 * @author: wff
 * @date:   2021年4月28日 下午3:16:01
 * @Copyright:
 */
public class roomPage extends QZ implements API {

	public String parameter; //参数集合
	public String enterpriseId; //企业id
	public String name; //会议室名称
	public String code; //会议室编码
	public String offset; //分页开始偏移量
	public String limit; //每页显示数量

	@Override
	public void initialize(HashMap<String, Object> data) {

	}

	@Override
	public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
		parameter = MapUtil.getValue("parameter", data);

		enterpriseId = MapUtil.getParameter_get(parameter,"enterpriseId").trim();
		if ((!enterpriseId.equals("")) && enterpriseId.equals("enterpriseId")) {
			enterpriseId=enterprise_Id;
			parameter = parameter.replace("enterpriseId=code", "enterpriseId="+ enterpriseId );
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

			if(msg.equals("SUCCESS")){

				//是否是线上环境
//				if (!isProduct) {
//
//				}
				//目前访问域名直接到奔奔内部接口没有经过奇瑞外部包装（现在返回字段为id），后期会更换域名到时候接口返回字段为s_UserToken
//				id = jp.getString("data.id");
//				s_UserToken = jp.getString("data.s_UserToken");

				//删除会前注册信息
//				if (data.get("CleanDB") != "" && data.get("CleanDB").equals("Y")) {
//					MongoDBUtil.deleteByid(data,"crystal","usrmgrAccount","BUid","feifei");
//				}
			}

		}
		if (result)
			return "Pass";
		else
			return "Fail:" + failReason;
	}
}
