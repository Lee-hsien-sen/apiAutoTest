package com.qingzi.listener;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;

import com.qingzi.TestData.qingzi_api_testData;
import com.qingzi.testUtil.Log;


public class SkipIInvokedMethodListener implements IInvokedMethodListener{
	//需求1，流程错误案例
	public static String Parameter_TCNO="";//当前场景名称
	public static String Parameter_ServiceUrl="";//错误的流程url
	public static HashMap<String,Integer> serviceUrlMap= new HashMap<String,Integer>();
	public static boolean Success=true;//场景测试的状态，发生错误false，未发生错误true
	
	
	//需求2，模块错误案例占比，数量
	public static Integer Parameter_TCNO_count=0;//存在错误的场景总数
	public static Integer SkipTestnumber=0;//错误用例次数
	public static HashMap<String,String> SkipMap= new HashMap<String,String>();//错误比例
	
	//测试结果是成功还是失败还是跳过
	public static Integer resultStatus=0;
	//运行之前
	@Override
	public void beforeInvocation(IInvokedMethod Method, ITestResult result) {
		//获取方法名称
		String MethodName=Method.getTestMethod().getMethodName();
		Object s[]=result.getParameters();
		if(s.length==1){//当可以获取到用例的时候
			String Step=getParameter(Arrays.toString(s),"Step");
			String TCNO=getParameter(Arrays.toString(s),"TCNO");
			String []Parameters=TCNO.split("_");
			TCNO=Parameters[0];
			
			//查看是否上一条是错误的（错误场景名称和当前场景名称一致），并且当前用例Step不等于1，都满足就跳过当前即将要运行的这条
			int IntegerStep=Integer.parseInt(Step.trim());
			if(IntegerStep!=1 && !Success && "f".equals(MethodName)){
				Log.logInfo("跳过");
				if(resultStatus!=3){
					recordTases(Parameter_ServiceUrl);//流程错误案例保存上一条的信息和次数
				}
				throw new SkipException("skip the test");
			}else if(IntegerStep==1 && TCNO.equals(Parameter_TCNO) && "f".equals(MethodName)){//遇到1，场景状态清除
				Success=true;
			}else if(IntegerStep==1 && !TCNO.equals(Parameter_TCNO) && "f".equals(MethodName) && Parameter_TCNO_count!=0){//计算该场景错误用例比例和该场景的错误案例数量
				Success=true;
				double TCNO_double=SkipTestnumber/(Parameter_TCNO_count*1.0);
				TCNO_double=m1(TCNO_double*100);
				SkipMap.put(Parameter_TCNO, TCNO_double+"____"+SkipTestnumber);
				SkipTestnumber=0;
				Parameter_TCNO="";
			}
		}
	}
	
	@Override
	public void afterInvocation(IInvokedMethod Method, ITestResult result) {
		try {
			resultStatus=result.getStatus();
			
			//获取方法名称
			String MethodName=Method.getTestMethod().getMethodName();
			Object s111[]=result.getParameters();
			String NO=getParameter(Arrays.toString(s111),"NO");
			if(s111.length==1){//当可以获取到用例的时候
				if(!result.isSuccess()  && "f".equals(MethodName)){
					SkipTestnumber++;
					Success=false;//记录本条用例错误，在下条用例前调用查看
					//只要有错误就记录，用例名称、url、步骤数		
					Object s[]=result.getParameters();
					Parameter_ServiceUrl=getParameter(Arrays.toString(s),"serviceUrl");
//					recordTases(Parameter_ServiceUrl);
					String TCNO=getParameter(Arrays.toString(s),"TCNO");
					String []Parameters=TCNO.split("_");
					Parameter_TCNO=Parameters[0];
					Parameter_TCNO_count=Integer.valueOf(Parameters[1]);
				}
				if(qingzi_api_testData.Testcount==Integer.valueOf(NO) && !Success){
					Success=true;
					double TCNO_double=SkipTestnumber/(Parameter_TCNO_count*1.0);
					TCNO_double=m1(TCNO_double * 100);
					SkipMap.put(Parameter_TCNO, TCNO_double+"____"+SkipTestnumber);
					SkipTestnumber=0;
					Parameter_TCNO="";
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	//记录测试过程中流程用例的错误数量
	public static void recordTases(String serviceUrlName){
		Integer value  = serviceUrlMap.get(serviceUrlName);
		int count=1;
		if(value==null){
			serviceUrlMap.put(serviceUrlName, count);
		}else{
			serviceUrlMap.put(serviceUrlName, ++value);
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
//					System.out.println("str="+Arrays.toString(str));
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

	public double m1(double f ) {
	      BigDecimal bg = null; //new BigDecimal(f);
	      bg = BigDecimal.valueOf(f);
	      double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	      return f1;
	}
}
