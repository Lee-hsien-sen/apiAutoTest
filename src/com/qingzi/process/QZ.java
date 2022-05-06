package com.qingzi.process;


import com.qingzi.system.system;
import com.qingzi.testUtil.ReadProperties;

import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Random;

public class QZ extends BasicsGM{

    //	public static boolean isProduct = Boolean.valueOf(((system) map.get("qingzi")).getIsProduct());
	public static String Token = "";//1V1token
	public static String enterprise_name = "企业-ff" + new Random().nextInt(9000)+ 1000;//企业名称
	public static String BU_id = "";// BUid
	public static String App_id = "";
	public static String dev = "1";
	public static String App_secret = "";
	public static String laravel_session="";//cookie
	public static String XSRF_token="";// XSRF-token
	public static String appId = "12345678";//目前开发那边也是写死的值
//	public static String enterprise_Id = "1-EP-48824092482932736";//开发环境 企业id
	public static String enterprise_Id = "1-EP-48824092482932736";//测试环境 企业id
	public static String s_UserToken;//usertoken  getToken接口返回
	public static String authKey;//秘钥  getToken接口返回
//	public static String userAccountId = "1-UA-134800448460349440";//测试环境用户 userAccountId  getToken接口返回
//	public static String hostNickName = "yxw@qq.com";//开发环境用户 发起者用户昵称
public static String hostNickName = "winff@qq.com";//香港测试环境用户昵称
//	public static String userAccountId = "1-UA-137252758432178176";//开发环境用户 userAccountId  getToken接口返回
	public static String userAccountId = "1-UA-137330527877390336";//香港测试用户 userAccountId  getToken接口返回
//	public static String userAccountIdByOther = "1-UA-119213892000796672";//测试环境参会人 userAccountId  getToken接口返回
	public static String userAccountIdByOther2 = "1-UA-119213892000796672";//测试环境参会人 userAccountId  getToken接口返回
	public static String userAccountIdByOther = "1-UA-147492636341383168";//香港测试环境参会人 userAccountId  getToken接口返回
	public static String meeting_Id; //会议id   createMeeting接口返回
	public static String m_Id; //会议短id   createMeeting接口返回
	public static String userId; //主持人id   createMeeting接口返回
	public static String sdk_RoomId; //主持人房间id  createMeeting接口返回
	public static String MR_Id;//MRId  会议室id
	public static String title_meeting = "会议-ff" + new Random().nextInt(9000)+ 1000;
	public static String mId_meeting;//Mid
	public static String pwd_meeting;//pwd
	public static String sdkAccountId; // 媒体  参会人AccountId
	public static String sdkAccountIdByOther; // 媒体  参会人userAccountId
	public static String sdkRoomId;//mediaInfo 媒体房间id
	public static String board_Id;//白板ID
	public static String s_UserTokenByOther;//参会人token
	public static String authKeyByOther;//参会人秘钥
	public static String groupId;//群组id


	static{
		//初始环境设置DNS
//		if(!cmdUtil.run("nslookup www.qq.com").contains("192.168.10.222")){
//			Log.logInfo("正在设置dns地址为：192.168.10.222,如果失败，请断开有限网络，用无线网络连接");
//			cmdUtil.run("netsh interface ip set dns \"无线网络连接(media-test)\" static 192.168.10.222");
//		}else{
//			Log.logInfo("dns地址正确无需设置");
//		}
		String mysql_local_Online= ReadProperties.GetTestPropertyByKey("mysql_local_Online");
		String system_nane= ReadProperties.GetTestPropertyByKey("system_nane");
		//本地数据库连接
		if(mysql_local_Online.equals("local")){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(((system)map.get(system_nane)).getSqlurl(),
						((system)map.get(system_nane)).getSqlname(),
						((system)map.get(system_nane)).getSqlpwd());
				stmt = conn.createStatement();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(mysql_local_Online.equals("Online")){
			//线上数据库连接
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(((system)map.get(system_nane)).getSqlurl(),
						((system)map.get(system_nane)).getSqlname(),
						((system)map.get(system_nane)).getSqlpwd());
				stmt = conn.createStatement();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//初始化数据录入
		if(!ReadProperties.isBoolean()){

		}
	}

	//清除map集合Enterprise_users(企业用户)，表名"xyu_users"，列名name，
		public void cleanEnterprise_usersFromDB() {
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//清除map集合room_nums，表名"xyu_room_users"，列名room_num，
			public void cleanRoom_numsFromDB() {
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		//清除map集合Business_Administrator（业务管理员），表名"xyu_users"，列名name，
		public void cleanBusiness_AdministratorFromDB() {

		}
}
