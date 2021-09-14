package test;

import java.text.SimpleDateFormat;
import java.util.Random;

import sun.net.www.http.HttpClient;

public class test1 {

	
	public static void main(String[] args) {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = dateformat.format(System.currentTimeMillis());
		System.out.println(dateStr);
		System.out.println(new Random().nextInt(90000000)+ 10000000);
	}
	

}
