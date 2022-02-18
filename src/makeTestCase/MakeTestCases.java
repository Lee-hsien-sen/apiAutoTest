package makeTestCase;

//支持get方式 

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;






import com.qingzi.testUtil.SheetUtils;

import net.sf.json.JSONObject;

public class MakeTestCases {
	
	public static int state=0;//代表有其他页面没有，0标示没有，1表示有一个，2表示有两个
	
	public static List<String> SheetList=new ArrayList<String>();//sheet页面的名称，里面存的是sheet2，sheet3之类的sheet页名称
	
	public static List<String> nameList=new ArrayList<String>();//包含sheet页面对应的字段名称
	
	public static List<String> List_1=new ArrayList<String>();//List_1
	public static List<String> List_4=new ArrayList<String>();//List_4
	public static List<String> List_5=new ArrayList<String>();//List_5
	
	public static List<List<String>> list_all=new ArrayList<List<String>>();//缺失字段，最后判断是否应该算正向还是反向
	
	public static void main(String[] args) {

		make("TestData/CaseMakeshushu.xls","Sheet1","post","qingziMtmgr");
		
	}
	
	
	public static void make(String FilePath,String SheetName,String getOrPost,String qudao){
		//获取用例
		List<String> listSum=TestCase(FilePath,SheetName);
        Set<String> set=new HashSet<String>();         
        set.addAll(listSum);
        listSum.clear();   
        listSum.addAll(set);
        List<String> TeseName= TestCaseName(listSum);
//        System.out.println("用例数"+listSum.size());
//        System.out.println("用例名字数"+TeseName.size());
        
		//获取接口全信息
		String AllPath="";
		try {
			AllPath=new readExcels().readExcels_path(FilePath,SheetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] apipath=AllPath.split("/");
		
		//获取接口类名
		String leiming=apipath[apipath.length-1];
		
		//获取接口路径
		int w=AllPath.indexOf("/");
		String path=AllPath.substring(w);
		
		String name=FilePath.split("/")[1];
		//获取接口名
		String jieKouMing=apipath[0];
		//生成测试用例
		for(int i=0;i<listSum.size();i++){
			String cases=listSum.get(i);
			if("get".equals(getOrPost)){
				cases=cases.replace(":", "=");
				cases=cases.replace("\"", "");
				cases=cases.replace(",", "&");
			}
			System.out.println(cases);	
			outCase(i,leiming,jieKouMing,TeseName.get(i),path,cases,name,"OutPage",getOrPost,qudao);
		}
				
	}
	//第一个参数是数量，第二个参数是接口类名，第三个参数是中文名
	public static void outCase(int i,String leiming,String jieKouMing,String TeseName,
			String path,String cases,String wenjianming,String Sheet,String getOrPost,String qudao){
		
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = sdf.format(d);
		
		//数据回写
		  SheetUtils sheet = new SheetUtils(wenjianming,Sheet);
		  sheet.writeExcel(
//				  		s,
				  		(i+1)+"",
				  		leiming + "_" + (i+1),
				  		(i+1)+"", 
				  		jieKouMing+"-"+TeseName,
				  		qudao,
					  	path,
						"N", 
						getOrPost,
						"200",
						cases
						);
	}
	
	//所有测试集合的用例名称
	public static List<String> TestCaseName(List<String> list){
		//所有测试集合的用例名称
		List<String> listt=new ArrayList<String>();
		
		List<String> list1_4=zhengxiang(List_1,List_4);
		List<String> list1_5=zhengxiang(List_1,List_5);
		
		
		//先把测试用例遍历
		for (int i=0;i<list.size();i++) {
			String name="";
//			System.out.println("用例名称："+listt.toString());
//			System.out.println();
//			System.out.println("测试用例："+list.get(i));
//			System.out.println("字段："+List_1.toString());
//			System.out.println("反向："+list1_5.toString());
			
			//每一条测试用例
			//查看是否缺少字段
			List<String> list_true=zhengxiang(list.get(i));
			name=baohan(List_1,list_true);
			if(!"未缺失".equals(name)){
				listt.add(name);
				continue;
			}
			
			name=list1_5(list1_5,list.get(i));
			if(!"正向用例".equals(name)){
				listt.add(name);
				continue;
			}
			
			name=list1_4(list1_4,list.get(i));
			listt.add(name);
			
		}
		return listt;
	}
	
	//获取正向所有精确字段
	public static List<String> zhengxiang(String stra){
		//预返回的集合
		List<String> listt=new ArrayList<String>();
		//循环获取到每一个字段的正确集合
		String ziduan []=stra.split(",");
		String zuo="";
		
		for(int i=0;i<ziduan.length;i++){
			zuo=ziduan[i];
			String zhong []=zuo.split(":");
			// 获取到每一个字段的正确中的一个2，3，4，5，6，
			if(zhong[0]!=null && zhong[0].length()>2){
				String a1=zhong[0].substring(1,zhong[0].length()-1);
				listt.add(a1);
			}
		}
		return listt;
	}
	
	//两个集合：list——1，list——4拼接成正向集合
	public static List<String> zhengxiang(List<String> list_1,List<String> list_4){
		//预返回的集合
		List<String> listt=new ArrayList<String>();
		String str="";
		//循环获取到每一个字段的正确集合
		for(int i=0;i<list_1.size();i++){
			// 获取到每一个字段的正确中的一个2，3，4，5，6，
			String zhengxiangfenjie []=list_4.get(i).split(",");
			for(int j=0;j<zhengxiangfenjie.length;j++){
				str="\""+list_1.get(i)+"\":"+zhengxiangfenjie[j];
				listt.add(str);
			}
		}
		return listt;
	}
	
	//用例中是否包含list——5中的信息，如果包含返回"反向用例-XXX"
	public static String list1_5(List<String> list1_5,String str){
		String queshideziduan="";
		for(int i=0;i<list1_5.size();i++){
			if(str.contains(list1_5.get(i))){
				queshideziduan=list1_5.get(i);
			}
		}
		if("".equals(queshideziduan)){
			return "正向用例";
		}else{
			return "反向用例-"+queshideziduan;
		}
	}
	
	
	//用例中是否包含list——4中的信息，如果包含返回"正向用例"
	public static String list1_4(List<String> list1_4,String str){
		String fenjie[] =str.split(",");
		//测试用例集合
		List<String> listSum=new ArrayList<String>();
		for(int i=0;i<fenjie.length;i++){
			listSum.add(fenjie[i]);
		}
		
		listSum.removeAll(list1_4);
//		System.out.println(listSum.toString());
		
		if(listSum.isEmpty()){
			return "正向用例";
		}else{
			return "反向用例-"+listSum.toString();
		}
	}
	
	//用例中是否包含list——1中的信息，如果没有返回XXXX字段缺失
	public static String baohan(List<String> list_1,List<String> list_true){
		//测试用例集合
		List<String> listSum=new ArrayList<String>();
		listSum.addAll(list_1);
		
		String queshideziduan="";
//		System.out.println(listSum.size());
//		System.out.println(list_true.size());list_all
		
		if(listSum.size()!=list_true.size()){
			listSum.removeAll(list_true);
			queshideziduan=listSum.toString();
		}
		
		if("".equals(queshideziduan)){
			return "未缺失";
		}else{
			queshideziduan=queshideziduan.substring(1, queshideziduan.length()-1);
			for(int i=0;i<list_all.size();i++){
				String NorY=list_all.get(i).toString();
//				System.out.println(NorY);
//				System.out.println((", "+queshideziduan+","));
//				System.out.println(NorY.contains(", "+queshideziduan+","));
//				System.out.println(NorY.contains(", N,"));
				if(NorY.contains(", "+queshideziduan+",") && NorY.contains(", N,")){
					return "正向用例-"+queshideziduan+"非必输项缺失";
				}
			}
			return "反向用例-"+queshideziduan+"缺失";
		}
	}
	
	public static List<String> TestCase(String path,String Sheet){
		//测试用例集合
		List<String> listSum=new ArrayList<String>();
		
		readExcels readExcels= new readExcels();
		List<List<String>> list=null;
		try {
			list=readExcels.readExcels_return(path,Sheet);
			list_all.addAll(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		List<Set<String>> listt=new ArrayList<Set<String>>();
		List<Set<String>> listf=new ArrayList<Set<String>>();
		
		for (int i=0;i<list.size();i++) {
			List<String> li=list.get(i);
			Set<String> sett=new HashSet<String>();
			Set<String> setf=new HashSet<String>();
			String name="";
			Object [] objt=null;
			Object [] objf=null;
			for (int j=0;j<li.size();j++) {
				if(j==1){
					name=li.get(j).trim();
					List_1.add(name);
				}
				
				if(j==4){
//					if("N".equals(li.get(j-1).trim())){
//						String str=(li.get(j).trim()+",null");
//						objt=str.split(",");
//						List_4.add(li.get(j).trim());
//						List_4.add(null);
//					}else{
						objt=li.get(j).trim().split(",");
						List_4.add(li.get(j).trim());
//					}
				}
				if(j==5){
					String s="";
					if("".equals(li.get(j).trim()) || li.get(j).trim()==null){
						s=""+li.get(2).trim()+","+li.get(3).trim();
						List_5.add(s);
					}else{
						s=""+li.get(j).trim()+","+li.get(2).trim()+","+li.get(3).trim();
						List_5.add(s);
					}
					objf=s.split(",");
				}
			}
			sett=getObject(name,objt,objt[0]+"");
			setf=getObject(name,objf,objt[0]+"");
					
			listt.add(sett);
			listf.add(setf);
		}
//		System.out.println("正向用例"+listt.toString());
//		System.out.println("反向用例"+listf.toString());
		
		//如果random_String.state不等于0说明存在其他sheet页面，如果是2就说明有其他两个sheet页
//		System.out.println(state);
//		System.out.println(SheetList);
//		System.out.println(nameList);
		
		for(int i=0;i<MakeTestCases.state;i++){
			List<Set<String>> a=TestCase_N("TestData/CaseMake.xls",SheetList.get(i),nameList.get(i));
			listt.add(a.get(0));
			listf.add(a.get(1));
		}	
			
		SheetList.clear();
		nameList.clear();
		
//		System.out.println(listt);
//		System.out.println(listf);
		
		//产生正向的用例
		Set<String> sum1=getZReslut(listt);
		Iterator it1 = sum1.iterator();
		String yongli="";
		while(it1.hasNext()){
//			System.out.println(qudouhao(it1.next()+""));
			yongli=(qudouhao(it1.next()+""));
			yongli=yongli.replace("~", ",");//将用例所有的波浪线替换回来，替换成逗号
			listSum.add(yongli);
		}
		
		//产生反向的用例,第一个参数是正确的参数集合支取一个，第二个参数集合是错误的集合
		for(int i=0;i<listf.size();i++){
			Set<String> sum2=getFReslut(sum1,listf.get(i));
//			System.out.println("sum2="+sum2);
			Iterator it2 = sum2.iterator();
			while(it2.hasNext()){
//				System.out.println(qudouhao(it2.next()+""));
				String ssss=(String) it2.next();
//				System.out.println(ssss);
				yongli=qudouhao(ssss+"");
				yongli=yongli.replace("~", ",");//将用例所有的波浪线替换回来，替换成逗号
//				System.out.println("yongli="+yongli);
				
				listSum.add(yongli);
				//System.out.println("listSum="+listSum);
			}
		}
//		System.out.println(listSum.toString());
		return listSum;
	}
	
	public static List<Set<String>> TestCase_N(String path,String Sheet,String ziduanname){
		//测试用例集合
		List<Set<String>> listSum=new ArrayList<Set<String>>();
		
		readExcels readExcels= new readExcels();
		List<List<String>> list=null;
		try {
			list=readExcels.readExcels_return(path,Sheet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Set<String>> listt=new ArrayList<Set<String>>();
		List<Set<String>> listf=new ArrayList<Set<String>>();
		
		for (int i=0;i<list.size();i++) {
			List<String> li=list.get(i);
			Set<String> sett=new HashSet<String>();
			Set<String> setf=new HashSet<String>();
			String name="";
			Object [] objt=null;
			Object [] objf=null;
			for (int j=0;j<li.size();j++) {
				if(j==1){
					name=li.get(j).trim();
				}
				if(j==4){
					objt=li.get(j).trim().split(",");
				}
				if(j==5){
					String s="";
					if("".equals(li.get(j).trim()) || li.get(j).trim()==null){
						s=""+li.get(2).trim()+","+li.get(3).trim();
					}else{
						s=""+li.get(j).trim()+","+li.get(2).trim()+","+li.get(3).trim();
					}
					objf=s.split(",");
				}
//				System.out.println(objf.toString());
			}
			sett=getObject(name,objt,objt[0]+"");
			setf=getObject(name,objf,objt[0]+"");
					
			listt.add(sett);
			listf.add(setf);
		}
		System.out.println("正向用例"+listt.toString());
		System.out.println("反向用例"+listf.toString());
		
		
		//正确和错误的集合
		Set<String> set_n_t=new HashSet<String>();
		Set<String> set_n_f=new HashSet<String>();
		//产生正向的用例
		Set<String> sum1=getZReslut(listt);
		Iterator it1 = sum1.iterator();
		String linshiyongli="";
		while(it1.hasNext()){
//			System.out.println(qudouhao(it1.next()+""));
			linshiyongli=qudouhao(it1.next()+"");
			linshiyongli=linshiyongli.replace(",", "~");
			set_n_t.add("\""+ziduanname+"\""+":{"+linshiyongli+"}");
		}
		
		//产生反向的用例,第一个参数是正确的参数集合支取一个，第二个参数集合是错误的集合
		for(int i=0;i<listf.size();i++){
			Set<String> sum2=getFReslut(sum1,listf.get(i));
			Iterator it2 = sum2.iterator();
			while(it2.hasNext()){
//				System.out.println(qudouhao(it2.next()+""));
				linshiyongli=qudouhao(it2.next()+"");
				linshiyongli=linshiyongli.replace(",", "~");
				set_n_f.add("\""+ziduanname+"\""+":{"+linshiyongli+"}");
			}
		}
		
		listSum.add(set_n_t);
		listSum.add(set_n_f);
		return listSum;
	}

	//去掉逗号
	public static String qudouhao(String str){
		str=str.trim();
		if(str.length()>0 && str.charAt(0)==','){
			str=str.substring(1);
		}else if(str.length()>0 && str.charAt(str.length()-1)==','){
			str=str.substring(0,str.length()-1);
		}
		return str;
		
	}
	
	//生成正确的用例
	public static Set<String> getZReslut(List<Set<String>> list){
		String str="";
		for(int i=0;i<list.size();i++){
			String str1="";
			Object[] obj=setjihe(list.get(i));
			str1=Arrays.toString(obj).trim();
			str=str+str1.substring(1, str1.length()-1)+"==".trim();
			
		}
		str=str.substring(0, str.length()-2);
		Set<String> result = HelloHongShu.getSet(str);
		return result;
	}
	public static Object[] setjihe(Set<String> set){
		Object[] obj=set.toArray();
		return obj;
	}
	
	//生成错误的用例
	public static Set<String> getFReslut(Set<String> set1,Set<String> set2){
		Set<String> set=new HashSet<String>();
		Object[] obj1=set1.toArray();
		Object[] obj2=set2.toArray();
		
		//获取正确用例的其中一个
		String zhengque=obj1[0]+"";
		String linshi="";
		String cuowulinshi="";
		Object [] zhengque1=zhengque.split(",");
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<zhengque1.length;i++){
			sb.append(zhengque1[i]);
			sb.append(",");
		}
		String jihe=sb.toString().substring(0,sb.toString().length()-1);
//		System.out.println(jihe);
		
		
		//用正确的用例为模板，找到与错误部分相同的一节内容进行替换操作
		for(int i=0;i<zhengque1.length;i++){
//			System.out.println(11111111);
//			for(int j=0;j<obj2.length;j++){
			for(int j=(obj2.length-1);j>=0;j--){
				//错误用例中分号前面的那段信息
				String cuowu=obj2[j]+"";
				cuowulinshi=cuowu.trim();
				cuowu=(cuowu.split(":")[0]).trim();
				
				linshi=(zhengque1[i]+"").trim();
				if(!linshi.contains(cuowu)){
					break;
				}
				zhengque=jihe;
//				System.out.println("被替代前="+zhengque);
//				System.out.println("被替代内容="+linshi);
//				System.out.println("替代为="+cuowulinshi);
				zhengque=zhengque.replace(linshi, cuowulinshi);
				zhengque=quchongfudouhao(zhengque);
				set.add(zhengque);
//				System.out.println("被替代后="+zhengque);
//				System.out.println("set="+set);
			}
		}
		return set;
	}
	public static String quchongfudouhao(String str){
		return str.replace(",,", ",");
	}
	
	//第一个参数是变量名，第二个参数是参数集合，第三个参数是正的需要被类型转换的参数
	public static Set<String> getObject(String name , Object[] obj,String s){
		
		
		
//		Object w1[]=new Object[obj.length];
		Set<String> set=new HashSet<String>();
		String linshi="";
		for(int j=0;j<obj.length;j++){
			if(obj[j]==null || "code".equals(obj[j])){
				linshi="\""+name+"\""+":"+obj[j];
			}else if("Y".equals(obj[j])){
				linshi="";
			}else if("N".equals(obj[j])){
				linshi="";
//				continue;
			}else if("String".equals(obj[j])){
				Long w=StringToInt(s);
//				System.out.println("w="+w);
				if(w!=0){
					linshi="\""+name+"\""+":"+w;
				}else{
					linshi="\""+name+"\""+":"+1;//字符串转换成int类型失败后，将字符串变为0
//					System.out.println(linshi);
				}
			}else if("int".equals(obj[j])){
				linshi="\""+name+"\""+":"+"\""+s+"\"";
			}else{
				linshi="\""+name+"\""+":"+""+obj[j]+"";
			}
//			System.out.println(linshi);
			set.add(linshi);
//			System.out.println(set);
		}
		return set;
	}
	
	
	public static Long StringToInt(String str){
		Long a=0L;
		try {
			a=Long.parseLong(str);
		} catch (Exception e) {
//			System.out.println("转换异常");
		}
		return a;
	}

}
