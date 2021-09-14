package com.qingzi.process;

import io.restassured.response.Response;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import net.sf.json.JSONObject;

import com.qingzi.testUtil.Log;
import com.qingzi.testUtil.XMLread;

public abstract class BasicsGM {
	
	//连接池
	public static Connection conn = null;
	public static Statement stmt = null;
//	
	public static Map<String,Object> map=new XMLread().getSystem();// 读取XML配置文件
//	
	public static boolean isProduct =  false;
//	
	//更改huimai.bestbuy_order表，条件是id=orderId，值该为orderStatus
	public static Integer updateOrderStatus(String fromName,String setCondition, String whereCondition) {
		Integer result=null;
		String sql =null;
		try {
			sql = " update  "+fromName+"  set  "+setCondition+"  where  " + whereCondition;
			result = stmt.executeUpdate(sql);//
			if (result == -1) {
				Log.logInfo("update failed! "+sql);
			}else{
				Log.logInfo("update  OK! " + sql);
			}
		} catch (SQLException e) {
			Log.logError("update failed! "+sql);
		}
		return result;
	}
	
	//查询表名tableName的条件是condition的结果集
	public ResultSet selectFromDB(String tableName, String condition) throws Exception {
		String sql;
		ResultSet rs = null;
		sql = " select  *  from  " + tableName + "  where  " + condition;
//		System.out.println(sql);
		rs = stmt.executeQuery(sql);
		return rs;

	}
	
	//查询表名tableName的条件是condition的结果集,返回数量
	public ResultSet selectCountFromDB(String tableName, String condition) throws Exception {
		String sql;
		ResultSet rs = null;
		sql = " select  count(*)  from  " + tableName + "  where  " + condition;
		Log.logInfo(sql);
		rs = stmt.executeQuery(sql);
		return rs;

	}
	
	//查询表名tableName的条件是condition的结果集
	public ResultSet sqlFromDB(String sql) throws Exception {
		ResultSet rs = stmt.executeQuery(sql);
		return rs;

	}
	
	//具体的删除动作，tableName=表名，key列，value值使用的like删除
	public void deleteFromDB(String tableName, String key, String value) throws Exception {
		String sql = "delete from " + tableName + " where " + key + " like '" + value + "'";
		Log.logInfo("sql==="+sql);
		int result = stmt.executeUpdate(sql);
		Log.logInfo("result==="+result);
		if (result == -1) {
			Log.logError("Clean failed!");
		}

	}
	
	
//	
	//判断字符串是否是空或者是“”
	public boolean StringIsNull(String str){
		if(str==null || "".equals(str)){
			return false;
		}else{
			return true;
		}
	}
	
	//获取errcode或code，或者msg或errmsg，返回去作为用例输出结果
	public String getCodeOrErrcode( Response re){
		try {
			String code=JSONObject.fromObject(re.body().asString()).getString("code");
			if(StringIsNull(code)){
				return code;
			}
		} catch (Exception e) {
			return "无code返回";
		}
		
		try {
		String errcode=JSONObject.fromObject(re.body().asString()).getString("errcode");
		if(StringIsNull(errcode)){
			return errcode;
		}
		} catch (Exception e) {
			return "无code返回";
		}
		return "";
	}
	//获取errcode或code，或者msg或errmsg，返回去作为用例输出结果
	public String getMsgOrErrmsg( Response re){
		try {
			String msg=JSONObject.fromObject(re.body().asString()).getString("msg");
			if(StringIsNull(msg)){
				return msg;
			}
		} catch (Exception e) {
		}
		
		try {
		String errmsg=JSONObject.fromObject(re.body().asString()).getString("errmsg");
		if(StringIsNull(errmsg)){
			return errmsg;
		}
		} catch (Exception e) {
		}
		
		try {
		String errmsg=JSONObject.fromObject(re.body().asString()).getString("message");
		if(StringIsNull(errmsg)){
			return errmsg;
		}
		} catch (Exception e) {
			return "无msg返回";
		}
		return "";
	}

}
