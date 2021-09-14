package com.qingzi.listener;

import java.util.Arrays;
import java.util.TreeSet;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
/**
 * method
 * //method.getDate()
 * @author Administrator
 *
 */
public class ProcessTestng implements IInvokedMethodListener {
	
	public static String NO="";
	public static String NOTE="";
	public static TreeSet<Integer> treeSet= new TreeSet<Integer>();
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult result) {
		Object s[]=result.getParameters();
		String TCNO=getParameter(Arrays.toString(s),"TCNO");
		String []Parameters=TCNO.split("_");
		String Parameter_before=Parameters[0];
		//获得用例编号
		String TestNO=getParameter(Arrays.toString(s),"NO");
		if(!("".equals(TestNO.trim()))){
			int a2=Integer.parseInt(TestNO.trim());
			if(NOTE.equals(Parameter_before)){
				int w=(a2-1);
				treeSet.add(w);
			}
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult result) {
		
//		System.out.println(result.isSuccess());
		try {
			if(result.isSuccess()){
			}else{
				//总体算法是第一个减去第二个数加1
				Object s[]=result.getParameters();
				//分离第二个参数的前后
				String TCNO=getParameter(Arrays.toString(s),"TCNO");
//				System.out.println("TCNO="+TCNO);
				String []Parameters=TCNO.split("_");
				String Parameter_before=Parameters[0];
				NOTE=Parameter_before;
				String Parameter_after=Parameters[1];
				int NOTE_NO=Integer.parseInt(Parameter_after.trim());
				//获取第一个参数的值
				String TestNO=getParameter(Arrays.toString(s),"NO");
				int Test_NO=Integer.parseInt(TestNO);
				//最终得数
				int su=Test_NO-NOTE_NO;
				//在记录本中记录
				for(int i=su;i<Test_NO;i++){
					treeSet.add(i);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("测试通过，没有错误用例标号,或编号不正确");
		}
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
				String[] str=strcomma[k].split("=");
				String str_strcomma=Arrays.toString(str);
//				System.out.println("str="+Arrays.toString(str));
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
}
