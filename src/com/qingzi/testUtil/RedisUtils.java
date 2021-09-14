package com.qingzi.testUtil;


import com.qingzi.process.BasicsGM;
import com.qingzi.system.system;

import redis.clients.jedis.*;

public class RedisUtils {

	public enum RedisPath{

		LOGINVERIFYCODE("bestbuy:customer:sendCode:app:login:"),
		SETDEALVERIFYCODE("bestbuy:customer:sendCode:app:setDeal:");
		private String path;

		RedisPath(String redisPath){
			this.path = redisPath;
		}

		public String toString(){
			return this.path.toString();
		}
	}

	//获取电话号码所对应的验证码采用redis方式
   public String getVerifyCode(String phoneNumber){
	   
	   system tem=(system)(BasicsGM.map.get("gmysx"));
	   String verifyCode=null;
		   Jedis jedis = new Jedis(tem.getRedis_URI());
		   jedis.auth("2xqPR2i7OoiKn5bs8tI0");
		   jedis.select(Integer.valueOf(tem.getRedis_db_index()));
		   verifyCode=jedis.get(("BESTBUY:CUST:PHONEMSG:"+phoneNumber));
		   if(verifyCode==null){
			   Log.logInfo("getVerifyCode Cannot get verifyCode not in Redis for "+ phoneNumber);
			   verifyCode="";
		   }
		   return verifyCode;
	}
   
   //获取自营渠道的图片验证码
   public String getZyVerifyCode(String phoneNumber){
	   if(phoneNumber==null){
		   phoneNumber="";
	   }
	   system tem=(system)(BasicsGM.map.get("zy"));
	   String verifyCode=null;
		   Jedis jedis = new Jedis(tem.getRedis_URI());
		   jedis.auth("2xqPR2i7OoiKn5bs8tI0");
		   jedis.select(Integer.valueOf(tem.getRedis_db_index()));
		   verifyCode=jedis.get(("bestbuy:customer:imgCode:app:register:"+phoneNumber));
		   if(verifyCode==null){
			   Log.logInfo("getZyVerifyCode Cannot get verifyCode not in Redis for "+ phoneNumber);
			   verifyCode="";
		   } 
		   Log.logInfo("自营渠道的图片验证码："+verifyCode);
		   return verifyCode;
	}
   
 //获取自营3.0渠道的图片验证码
   public String getZy3VerifyCode(String phoneNumber){
	   if(phoneNumber==null){
		   phoneNumber="";
	   }
	   system tem=(system)(BasicsGM.map.get("zy3"));
	   String verifyCode=null;
		   Jedis jedis = new Jedis(tem.getRedis_URI());
		   jedis.auth("2xqPR2i7OoiKn5bs8tI0");
		   jedis.select(Integer.valueOf(tem.getRedis_db_index()));
		   verifyCode=jedis.get(("bestbuy:customer:imgCode:app:register:v1:"+ phoneNumber));
		   if(verifyCode==null){//
			   Log.logInfo("getZyVerifyCode Cannot get verifyCode not in Redis for "+ phoneNumber);
			   verifyCode="";
		   } 
		   Log.logInfo("自营渠道的图片验证码："+verifyCode);
		   return verifyCode;
	}
   
   //自营渠道的短信验证码bestbuy:customer:sendCode:app:register
   public String getZyDuanXinCode(String phoneNumber){
	   system tem=(system)(BasicsGM.map.get("zy"));
	   String verifyCode=null;
		   Jedis jedis = new Jedis(tem.getRedis_URI());
		   jedis.auth("2xqPR2i7OoiKn5bs8tI0");
		   jedis.select(Integer.valueOf(tem.getRedis_db_index()));
		   verifyCode=jedis.get(("bestbuy:customer:sendCode:app:register:"+phoneNumber));
		   if(verifyCode==null){
			   Log.logInfo("getZyDuanXinCode Cannot get verifyCode not in Redis for "+ phoneNumber);
			   verifyCode="";
		   }
		   Log.logInfo("自营渠道的短信验证码："+verifyCode);
		   return verifyCode;
	}


	//通用方法
	public String getRedisCode(RedisPath redisPath,String phoneNumber){
		system tem=(system)(BasicsGM.map.get("zy"));
		String verifyCode=null;
		Jedis jedis = new Jedis(tem.getRedis_URI());
		jedis.auth("2xqPR2i7OoiKn5bs8tI0");
		jedis.select(Integer.valueOf(tem.getRedis_db_index()));
		verifyCode=jedis.get((redisPath.toString()+phoneNumber));
		if(verifyCode==null){
			Log.logInfo("获取验证码失败，电话： "+ phoneNumber);
			verifyCode="";
		}
		Log.logInfo("获取的验证码为："+verifyCode);
		return verifyCode;
	}

	//获取蛋壳的注册验证码
	   public String getDkVerifyCode(String phoneNumber){
		   if(phoneNumber==null){
			   phoneNumber="";
		   }
		   
		   system tem=(system)(BasicsGM.map.get("dk"));
		   String verifyCode=null;
			   Jedis jedis = new Jedis(tem.getRedis_URI());
			   jedis.auth("2xqPR2i7OoiKn5bs8tI0");
			   jedis.select(Integer.valueOf(tem.getRedis_db_index()));
			   verifyCode=jedis.get(("BESTBUY:CUST:PHONEMSG:"+ phoneNumber));
			   if(verifyCode==null){
				   Log.logInfo("getDkVerfyCode Cannot get verifyCode not in Redis for "+ phoneNumber);
				   verifyCode="";
			   } 
			   Log.logInfo("蛋壳注册验证码为："+verifyCode);
			   return verifyCode;
		}
	
   public static void main(String[] args) {
	   RedisUtils ru=new RedisUtils();
	   System.out.println(ru.getZy3VerifyCode("13910960649"));
//	   System.out.println(getZyDuanXinCode("13910960649"));
   }
}
