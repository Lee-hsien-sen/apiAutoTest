package test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.qingzi.testUtil.MapUtil;


public class map_show {
	
	public static void main(String[] args) throws Exception {
		Map<String,String> map=new HashMap<String,String>();
		map.put("a","1");
		map.put("b","2");
		map.put("c","3");
		System.out.println(showMap(map,"d"));
		
//		String ss=show();//把map中parameter对应的内容拿出来
//		String sss=getPhone(ss,"phone");//在parameter中查看，是否有关键字
//		System.out.println("sss="+sss+"==");
//		if(sss==null){
//			System.out.println("等于空");
//		}
//		if(sss.equals("")){
//			System.out.println("等于空字符串");
//		}
	}
	//查找map中是否有这个建
		public static boolean showMap(Map<String, String> data,String key){
			Set<Map.Entry<String,String>> set=data.entrySet();
			Iterator<Entry<String, String>> it=set.iterator();
			while(it.hasNext()){
				Map.Entry<String,String> me=it.next();
				if(me.getKey().equals(key)){
					return true;
				}
			}
			return false;
		}
	
	public static String getPhone(String parameter,String phone){
		String[] strcomma=parameter.split(",");
		int comma=strcomma.length;
		StringBuffer sb=new StringBuffer();
		for(int k=0;k<comma;k++){
			//此时是多个，，，
			String[] str=strcomma[k].split(":");
			String str_strcomma=Arrays.toString(str);
			System.out.println("str="+Arrays.toString(str));
			//按参数传过来的字符串做为子串，在以逗号为节点的串中分别查找子串的关键字，
			//在找到后的位置开始查找数字，最后把数字的字符串返回
			if(str_strcomma.contains(phone)){
				for (int i =0;i< str_strcomma.length(); i++) {
					if (Character.isDigit(str_strcomma.charAt(i))) {
						sb.append(str_strcomma.charAt(i));
					}
				}
			}
		}
		return sb.toString();
	}
	
	//查看二维数组
	public static String show() throws Exception{
		//查看二维数组
		ReadExcels readExcels = new ReadExcels("DataAll.xls","TestCase");
		Object[][] arrmap= readExcels.readExcels_return();
		for(int i=0;i<arrmap.length;i++){
			for(int j=0;j<arrmap[i].length;j++){
				System.out.print(","+arrmap[i][j]);
			}
			System.out.println();
		}
//				HashMap<String, Object> hm=new HashMap<String, Object>();
//				hm=(HashMap<String, Object>) arrmap[0][0];
		@SuppressWarnings("unchecked")
		String s=MapUtil.getValue("parameter",(HashMap<String, Object>) arrmap[38][0]);
		System.out.println("s="+s);
		return s;
	}
}
