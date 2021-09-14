package com.qingzi.testUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.qingzi.system.system;




public class XMLread {

	public static void main(String[] args) {
		XMLread xml=new XMLread();
		
		Map<String,Object> map=(Map<String, Object>) xml.getSystem();
		system ysx=(system) map.get("Gm_ysx");
		system dd=(system) map.get("Gm_dd");
		
		System.out.println(ysx.getRM_port());
		System.out.println(dd.getRM_port());
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> getSystem(){
		Document document = null;
		Object obj=null;
		Method met=null;
		Map<String,Object> map=new HashMap<String,Object>();
		
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(new File("src/resources/system.xml")); // 读取XML文件,获得document对象
			// 获取根节点
			Element root = document.getRootElement();
			// 获取根节点下的子节点
			for (Iterator i = root.elementIterator(); i.hasNext();) {
				// 将每个子节点赋给el
				Element el = (Element) i.next();
				// 如果节点的名称为“system”，system元素属性name=参数// && systemName.equals(el.attribute("name").getValue())
				if ("system".equals(el.getName())) {// 获取节点元素的名称
					Class c=Class.forName("com.qingzi.system.system");
					obj=(Object) c.newInstance();
					for (Iterator it = el.elementIterator(); it.hasNext();) {
						Element elchild = (Element) it.next();
						met=c.getDeclaredMethod(getMethodName(elchild.getName()), String.class);
						met.invoke(obj, elchild.getText());		
						map.put(el.attribute("name").getValue(), obj);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}
	//获取注入得方法名
		private String getMethodName(String str){
			String s="set"+str.substring(0,1).toUpperCase()+str.substring(1,str.length());
			return s;	
		}

}
