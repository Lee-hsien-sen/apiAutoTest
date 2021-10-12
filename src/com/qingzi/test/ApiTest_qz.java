package com.qingzi.test;

import io.restassured.response.Response;

import java.sql.SQLException;
import java.util.HashMap;

import net.sf.json.JSONObject;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.qingzi.TestData.qingzi_api_testData;
import com.qingzi.interfaces.API;
import com.qingzi.listener.ProcessTestng;
import com.qingzi.listener.ResultTestng;
import com.qingzi.process.BasicsGM;
import com.qingzi.process.QZ;
import com.qingzi.testUtil.Log;
import com.qingzi.testUtil.MapUtil;
import com.qingzi.testUtil.Reflect_api;
import com.qingzi.testUtil.SheetUtils;
import com.qingzi.testUtil.StringUtils;
import com.qingzi.testUtil.XMLread;

@Listeners({ ProcessTestng.class,ResultTestng.class })
public class ApiTest_qz extends QZ{

  @Test(dataProvider = "renmai", dataProviderClass = qingzi_api_testData.class)
  public void f(HashMap<String, Object> data) {
	  
	  Log.logInfo(data.get("TCNO").toString() + " Step " + data.get("Description").toString() + " is running......");
	 
	  API obj = new Reflect_api().Reflections(data);
	  BasicsGM.map=new XMLread().getSystem();
	  
	  obj.initialize(data);
	  
	  data = obj.handleInput(data);
	  String parameter = MapUtil.getValue("parameter", data);
	 
	  Long startTime=System.currentTimeMillis();
	  Response re = obj.SendRequest(data, data.get("serviceUrl").toString(), data.get("Request").toString());
	  Long endTime=System.currentTimeMillis();
	  
	  String time=(endTime-startTime)+"毫秒";
	  
	  String body=re.asString();
	  
	  String codeORerrcode="";
	  String msgORerrmsy="";
	  String result = "";
	  
	  
	  if(body.contains("<title>")){
		int Alength="<title>".length();
		int start=body.indexOf("<title>");
		int end=body.indexOf("</title>")+1;
		body="页面标题："+body.substring(start+Alength, end-1);
		result=body;
		if(data.get("Description").toString().contains("流程")){
			result = obj.handleOutput(re, data);
		}
	  }else{
		  result = obj.handleOutput(re, data);
	  }
	  codeORerrcode=getCodeOrErrcode(re);
	  msgORerrmsy=getMsgOrErrmsg(re);
	  
	  Log.logInfo("返回结果="+StringUtils.decodeUnicode(body));
	  System.out.println();
	  
	  
	  //数据回写
	  HashMap<String, Object> ExpectResult=MapUtil.Expect(data);
	  SheetUtils sheet = new SheetUtils("DataAllshujie.xls", "Output");
	  sheet.writeExcel(
			  		data.get("NO").toString(),
				  	data.get("TCNO").toString() + "_Step" + data.get("Step").toString(),
				  	data.get("Description").toString(),
				  	parameter,
					JSONObject.fromObject(ExpectResult).toString(),
					StringUtils.decodeUnicode(re.asString()),
					codeORerrcode,
					msgORerrmsy,
					result,
					time
					);
	  if(result.indexOf("Fail")!=-1){
		  String Expect1=data.get("code")==null?"":data.get("code").toString();
		  String Expect2=data.get("msg")==null?"":data.get("msg").toString();
		  String Expect3=data.get("custom")==null?"":data.get("custom").toString();
		  Assert.assertEquals(StringUtils.decodeUnicode(body),Expect1+","+Expect2+","+Expect3);
	  }else{
		  Assert.assertTrue(true);
	  }
  }

  @AfterClass
  public void afterClass() {
		//测试结束删除测试所用的数据
	  if (!isProduct) {
//		  cleanEnterprise_usersFromDB();//清除企业用户
//		  cleanBusiness_AdministratorFromDB();//清除企业管理员
//		  ReadProperties.ClearProperty();//清除环境配置文件
		  try { 
				if(stmt!=null){
					stmt.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	  }
	  Log.logInfo("========测试结束========");
	  
  }

}
