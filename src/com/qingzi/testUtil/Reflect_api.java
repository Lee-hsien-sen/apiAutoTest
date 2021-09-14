package com.qingzi.testUtil;

import java.util.HashMap;

import com.qingzi.interfaces.API;



/**
 * 反射工具类
 * 
 * @author yaoshuai
 *
 */
public class Reflect_api {

	public API Reflections(HashMap<String, Object> data) {
		String ClassName = Reflect_api.getRefleserviceUrlxClassName(data);
		API obj = null;
		Class<?> c = null;
		int count=1;

		while(true){
			try {
				c = Class.forName(ClassName);
				break;
			}catch (ClassNotFoundException e) {
				String serviceUrl = data.get("serviceUrl").toString();
				String CalssName_new = serviceUrl.split("/")[serviceUrl.split("/").length - 1];

				String system = MapUtil.getValue("system", data);
				
				String classRoute = ReadProperties.GetPropertyByKey(system);
				
				ClassName=classRoute+"process."+CalssName_new;
//				Log.logError("Reflect_api,ReflexClass Error，Classname=" + ClassName);
//				String system = MapUtil.getValue("system", data);
//				String classRoute = ReadProperties.GetPropertyByKey(system);
//				count++;
//				if(!classRoute.equals("com.offcn.api.xyzb.liucheng")){
////					String serviceUrl = data.get("serviceUrl").toString();
////					ClassName = serviceUrl.split("/")[serviceUrl.split("/").length - 1];
//					String classRoute1 = ReadProperties.GetPropertyByKey("gmysx");
//					ClassName=ClassName.replaceAll(classRoute, classRoute1);
//				}
				
			} 
			if(count==3){
				break;
			}
			
		}
		
		
		try{
			obj = (API) c.newInstance();
		}catch (InstantiationException e) {
			Log.logError("Reflect_api,instantiation Error，Classname="
					+ ClassName);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.logError("Reflect_api,Reflection class is not public Error，Classname="
					+ ClassName);
			e.printStackTrace();
		}
		return obj;

	}

	// 获得数据中serviceUrl所对应的类名
	private static String getRefleserviceUrlxClassName(
			HashMap<String, Object> data) {
		String serviceUrl = data.get("serviceUrl").toString();
		String CalssName = serviceUrl.split("/")[serviceUrl.split("/").length - 1];

		String system = MapUtil.getValue("system", data);
		
		String classRoute = ReadProperties.GetPropertyByKey(system);
//		if ("/order/insert".equals(serviceUrl)) {
//		if (serviceUrl.indexOf("/order/insert")!=-1) {
//			return classRoute+CalssName + "_dd";
//		}

		//xuwen-添加对商户（sh）的支持
//		if("zy".equals(system) || "sh".equals(system) || "dk".equals(system) ){
//			String classReflectName = ReadProperties.GetPropertyByClassName(serviceUrl);
//			if (	classReflectName!=null 	) {
//				return classRoute+ classReflectName;
//			}
//		}
		if("test".equals(system)){
			String classReflectName = ReadProperties.GetPropertyByClassName(serviceUrl,system);
			if (	classReflectName!=null 	) {
				return classRoute+ classReflectName;
			}
		}
		return classRoute+CalssName;
	}
}
