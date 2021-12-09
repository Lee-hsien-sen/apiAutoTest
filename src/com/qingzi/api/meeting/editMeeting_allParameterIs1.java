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
 * @ClassName:  editMeeting
 * @Description:TODO  修改会议信息，全部参数值都是1
 * @author: wss
 * @date:   2021年12月9日16:12:00
 * @Copyright:
 */
public class editMeeting_allParameterIs1 extends QZ implements API {
	
	public String parameter; //参数集合
	public String meetingId; //解决方案会议室Id
	public String meetingManage; //会议meetingManage，包含meetingAudioStatus 和 meetingVideoStatus
	public String operated; //与被操作人
	public String meetingPermission; // meetingPermission,包含lock、waitingRoom、onlyHostShare
	public String participantPermission; //包含unmute、shartVideo、rename
	public String title; //会议标题

	@Override
	public void initialize(HashMap<String, Object> data) {

	}

	@Override
	public HashMap<String, Object> handleInput(HashMap<String, Object> data) {
		parameter = MapUtil.getValue("parameter", data);

		meetingId = MapUtil.getParameter(parameter,"meetingId").trim();
		title = MapUtil.getParameter(parameter,"title").trim();
		participantPermission = MapUtil.getParameter(parameter,"participantPermission").trim();
		meetingManage = MapUtil.getParameter(parameter,"meetingManage").trim();
		meetingPermission = MapUtil.getParameter(parameter,"meetingPermission").trim();

		if(!title.equals("") && title.equals("code")){
			parameter = parameter.replace("\"title\":code", "\"title\":\""+ title + "\"");
		}
		if(!meetingId.equals("") && meetingId.equals("code")){
			meetingId = meeting_Id; 
			parameter = parameter.replace("\"meetingId\":code", "\"meetingId\":\""+ meetingId + "\"");
		}

		if(!participantPermission.equals("") && participantPermission.equals("code")){
			HashMap<String, String> userMap = new HashMap<String, String>();
			userMap.put("unmute", "1");
			userMap.put("startVideo", "1");
			userMap.put("rename", "1");
			parameter = parameter.replace("\"participantPermission\":code", "\"participantPermission\":"+ JSONObject.fromObject(userMap) );
		}

		if(!meetingManage.equals("") && meetingManage.equals("code")){
			HashMap<String, String> userMap = new HashMap<String, String>();
			userMap.put("meetingAudioStatus", "1");
			userMap.put("meetingVideoStatus", "1");
			parameter = parameter.replace("\"meetingManage\":code", "\"meetingManage\":"+ JSONObject.fromObject(userMap) );
		}

		if(!meetingPermission.equals("") && meetingPermission.equals("code")){
			HashMap<String, String> userMap = new HashMap<String, String>();
			userMap.put("lock", "1");
			userMap.put("waitingRoom", "1");
			userMap.put("onlyHostShare", "1");
			parameter = parameter.replace("\"meetingPermission\":code", "\"meetingPermission\":"+ JSONObject.fromObject(userMap) );
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
		headers.put("dev","1");
		
		MyRequest myRequest = new MyRequest();
		myRequest.setUrl("/moms/mtmgr/v1/mcc/editMeeting");
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
				if (data.get("CleanDB") != "" && data.get("CleanDB").equals("Y")) {
					
					//先查询该用户创建的个人会议
					Document doc =  MongoDBUtil.findByid(data, "crystal", "usrmgrAccount", "BUid", BU_id);
					String personalRoomId = doc.getString("personalRoomId");
//					System.out.println(personalRoomId);
					//删除企业
					MongoDBUtil.deleteByid(data, "crystal", "usrmgrEnterprise", "name", enterprise_name);
					//删除个人注册后创建的个人会议室
					MongoDBUtil.deleteByid(data, "crystal", "mcmuMeetingRoom", "_id", personalRoomId);
					//删除会前注册信息
					MongoDBUtil.deleteByid(data,"crystal","usrmgrAccount","BUid", BU_id);
					//删除会议记录
					MongoDBUtil.deleteByid(data, "crystal", "mtmgrMeetingAuthLog", "meetingId", meetingId);
					//删除参会表
					MongoDBUtil.deleteByid(data, "crystal", "mtmgrMeetingParticipant", "accountId", userAccountId);
					//删除新建会议
					MongoDBUtil.deleteByid(data, "crystal", "mtmgrMetting", "title", title_meeting);
				}
			}
			
		}
		if (result)
			return "Pass";
		else
			return "Fail:" + failReason;
	}
}
