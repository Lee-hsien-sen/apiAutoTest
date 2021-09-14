package com.qingzi.testUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONObject;

public class MapUtil {

	public static void main(String[] args) {
		HashMap<String, Object> hm=new HashMap<String, Object>();
		hm.put("1", "a1");
		hm.put("statusCode", "a2");
		hm.put("3", "a3");
		hm.put("errcode", "a4");
		hm.put("5", "a5");
		hm.put("errmsg", "a6");
		HashMap<String, Object> newhm=Expect(hm);
		System.out.println("@AfterClass_data_ext="+JSONObject.fromObject(newhm).toString());
	}
	
	 //降序排序
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
            {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    //升序排序
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAscending(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
            {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

	//马洪亮
	// 生成数组格式的json串
		public static String getArrayJson(String para) {
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			if (para.contains(";")) {
				for (int i = 0; i < para.split(";").length; i++) {
					sb.append("{");
					sb.append(para.split(";")[i]);
					sb.append("},");
				}
				sb.deleteCharAt(sb.length() - 1);
			}else{
				sb.append("{");
				sb.append(para);
				sb.append("}");
			}
			
			sb.append("]");
			return sb.toString();
		}
	
	//显示整个map
	public static String showMap(HashMap<String, Object> data){
		Set<Map.Entry<String,Object>> set=data.entrySet();
		Iterator<Entry<String, Object>> it=set.iterator();
		HashMap<String, Object> newdata=new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
		while(it.hasNext()){
			Map.Entry<String,Object> me=it.next();
			String key=me.getKey();
			Object value=me.getValue();
			sb.append(key+":"+value+",");
		}
		return sb+"";
	}
	
	//显示整个map
		public static String showMap_String(Map<String, String> data){
			Set<Map.Entry<String,String>> set=data.entrySet();
			Iterator<Entry<String, String>> it=set.iterator();
			HashMap<String, String> newdata=new HashMap<String, String>();
			StringBuffer sb=new StringBuffer();
			while(it.hasNext()){
				Map.Entry<String,String> me=it.next();
				String key=me.getKey();
				String value=me.getValue();
				sb.append(key+":"+value+",");
			}
			return sb+"";
		}
	
	//返回期望结果
	public static  HashMap<String, Object> Expect(HashMap<String, Object> data){
		Set<Map.Entry<String,Object>> set=data.entrySet();
		Iterator<Entry<String, Object>> it=set.iterator();
		HashMap<String, Object> newdata=new HashMap<String, Object>();
		while(it.hasNext()){
			Map.Entry<String,Object> me=it.next();
			String key=me.getKey();
			Object value=me.getValue();
			if(key.equals("statusCode") || key.equals("code") || key.equals("msg") || key.equals("custom")){
				newdata.put(key, value);
			}
		}
		return newdata;
	}
	//获得数据中的Parameter，并修改成json模式
		public static String getParameter(HashMap<String, Object> data){
			String s=MapUtil.getValue("parameter",data);
			StringBuffer sb=new StringBuffer();
			sb.append("{");
			sb.append(s);
			sb.append("}");
			return sb.toString();
		}
		
	//讲list中map的值取出来，变成list
	public static List<Object> getOthersVerifyCode(List<Map<String, Object>> list) {
		Map map = new HashMap<String, Object>();
		List<Object> List=new ArrayList<Object>();
		for (int i=0;i<list.size();i++)
		{
		    Map  map1=(Map)list.get(i);
		    Iterator iterator = map1.keySet().iterator();
		    while (iterator.hasNext())
		    {
		        String key = (String) iterator.next();
		        Object object = map1.get(key);
		        List.add(object);
		    }
		}
		return List;
	}
	
	//讲list中map的值取出来，并且键包含在str字符串里的，取出来变成list
	public static List<Object> getStringList(List<Map<String, Object>> list,String [] str) {
		Map map = new HashMap<String, Object>();
		List<Object> List=new ArrayList<Object>();
		for (int i=0;i<list.size();i++)
		{
		    Map  map1=(Map)list.get(i);
		    Iterator iterator = map1.keySet().iterator();
		    while (iterator.hasNext())
		    {
		    	String key = (String) iterator.next();
		    	for(int j=0;j<str.length;j++){
		    		if(str[j].equals(key)){
		    			Object object = map1.get(key);
		    			List.add(object);
		    			break;
		    		}
		    	}
		    }
		}
		return List;
	}
	
	//讲list中的值取出来，bu包含在str字符串里的，取出来变成list
	public static List<Object> getNoStringList(List<Object> list,String str) {
    	for(int j=0;j<list.size();j++){
    		String jp=list.get(j).toString();
    		if(jp.contains(str)){
    			list.remove(j);
    			j--;
    		}
    	}
		return list;
	}	
		
	//获取verifyCode-map中指定的key所对应的值
//	public static String getOldVerifyCode(String phone) {
//			String str="";
//			str=BasicsGM.verifyCode.get(phone);
//			return str;
//	}
	
	//获取map中指定的key所对应的值,返回字符串
	public static String getValue(String ownPhoneNumber,HashMap<String, Object> data) {
			Object verifycode = null;
			Iterator<Entry<String, Object>> it = data.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = it.next();
				if (entry.getKey().equals(ownPhoneNumber)) {
					verifycode = entry.getValue();
//					System.out.println(verifycode);
					break;
				}
			}
			return verifycode+"";
	}
	//在parameter中查看，是否有（第二个参数）电话的关键字，有的话返回数字值
	public static String getNumber(String parameter,String number){
		String[] strcomma=parameter.split(",");
		int comma=strcomma.length;
		StringBuffer sb=new StringBuffer();
		for(int k=0;k<comma;k++){
			//此时是多个，，，
			String[] str=strcomma[k].split(":");
			String str_strcomma=Arrays.toString(str);
//			System.out.println("str="+Arrays.toString(str));
			//按参数传过来的字符串做为子串，在以逗号为节点的串中分别查找子串的关键字，
			//在找到后的位置开始查找数字，最后把数字的字符串返回
			if(str_strcomma.contains(number)){
				int start=str_strcomma.indexOf(',');
				str_strcomma=str_strcomma.substring(start+1,str_strcomma.length()-1);
				for (int i =0;i< str_strcomma.length(); i++) {
					if (Character.isDigit(str_strcomma.charAt(i))) {
						sb.append(str_strcomma.charAt(i));
					}
				}
			}
		}
		return sb.toString();
	}
	
	//在parameter中查看，是否有(第二个参数)verifyCode的关键字，有的话返回他的字母值
	public static String getParameter(String parameter,String Letter){
		if(parameter==null){
			return "";
		}
		String[] strcomma=parameter.split(",");
		int comma=strcomma.length;
		StringBuffer sb=new StringBuffer();
		for(int k=0;k<comma;k++){
			//此时是多个，，，
			String[] str=strcomma[k].split(":");
			String str_strcomma=Arrays.toString(str);
//			System.out.println("str="+Arrays.toString(str));
			//按参数传过来的字符串做为子串，在以逗号为节点的串中分别查找子串的关键字，
			//在找到后的位置开始查找数字，最后把数字的字符串返回
			if(str_strcomma.contains(Letter)){
				int start=str_strcomma.indexOf(',');
				sb.append(str_strcomma.substring(start+2,str_strcomma.length()-1));
				return sb.toString();
			}
		}
		return sb.toString();
	}
	
	//在parameter中获取get请求中的参数
  	public static String getParameter_get(String parameter,String Letter){
  		if(parameter==null){
  			return "";
  		}
  		String[] strcomma=parameter.split("&");
  		int comma=strcomma.length;
  		StringBuffer sb=new StringBuffer();
  		for(int k=0;k<comma;k++){
  			String str_strcomma=strcomma[k];
  			if(str_strcomma.contains(Letter)){
  				int start=str_strcomma.indexOf('=');
  				sb.append(str_strcomma.substring(start+1,str_strcomma.length()));
  				return sb.toString();
  			}
  		}
  		return sb.toString();
  	}
	
	//在parameter中查看，是否有(第二个参数)verifyCode的关键字，有的话返回他的字母值
		public static String getParameter_accurate(String parameter,String Letter){
			if(parameter==null){
				return "";
			}
			String[] strcomma=parameter.split(",");
			int comma=strcomma.length;
			StringBuffer sb=new StringBuffer();
			for(int k=0;k<comma;k++){
				//此时是多个，，，
				String str=strcomma[k].split(":")[0];
				String str_strcomma= MapUtil.killQuotes(str, "\"");//去掉双引号的education
//				String String str_strcomma==str;
//				System.out.println("str="+Arrays.toString(str));
				//按参数传过来的字符串做为子串，在以逗号为节点的串中分别查找子串的关键字，
				//在找到后的位置开始查找数字，最后把数字的字符串返回
				if(str_strcomma.equals(Letter)){
					try {
						sb.append(strcomma[k].split(":")[1]);
					} catch (Exception e) {
						Log.logInfo("字段没有后半部分");
					}
					return sb.toString();
				}
			}
			return sb.toString();
		}
	
	//去掉字母值的指定符号，参数1字符串，参数2指定符号
	public static String  killQuotes(String parameter,String Letter){
		StringBuffer sb=new StringBuffer();
		for(int k=0;k<parameter.length();k++){
			if(parameter.charAt(k)!= '"'){
				sb.append(parameter.charAt(k));
			}
		}
		return sb.toString();
	}	
	
	//去掉字母值的指定符号，参数1字符串，参数2指定符号
	public static String  killQuotes(String parameter,char Letter){
		StringBuffer sb=new StringBuffer();
		for(int k=0;k<parameter.length();k++){
			if(parameter.charAt(k)!= Letter){
				sb.append(parameter.charAt(k));
			}
		}
		return sb.toString();
	}	
	
	
	
	//在parameter中，按照第二个参数进行分割，分割成两段，获取后面那段的内容
		public static String getParameterList(String parameter,String Letter){
			if(parameter==null || parameter.equals("")){
				return "";
			}
			String[] strcomma=parameter.split(Letter);
			if(strcomma.length>0 && strcomma[1]!=null){
				return strcomma[1].toString();
			}
			return "";
		}



}
