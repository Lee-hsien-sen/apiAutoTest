package com.qingzi.testUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MyDateUtil {

	public static void main(String[] args) {
		getTime(24,0,0);
	}
	
	//按需生成字符串时间，参数是小时、分、秒,最好别有进位
	public static String getTime(int hour,int minute,int seconds) {
		Calendar calendar = Calendar.getInstance();
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH) + 1;
//		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.HOUR_OF_DAY,(calendar.get(Calendar.HOUR_OF_DAY)+hour)>22?22:calendar.get(Calendar.HOUR_OF_DAY)+hour);
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+minute);
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND)+seconds);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
//		System.out.println(format.format(calendar.getTime()));
		if(calendar.get(Calendar.HOUR_OF_DAY)==0){
			throw new RuntimeException("只能创建当日房间，今日时间不足，请休息一会儿吧~");
		}
		
		return format.format(calendar.getTime());
	}

	//返回当前时间字符串
	public static String getTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = sdf.format(d);
		return s;
	}

}
