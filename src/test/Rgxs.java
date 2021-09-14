package test;


import static io.restassured.http.ContentType.JSON;

import java.util.List;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

/**
 * 人工信审工具类
 * @author puhui
 *
 */
public class Rgxs{
//	public  String JSESSIONID;
//	public  String StatusCode;
//	public  String time;

	public static void main(String[] args) {
		Rgxs r=new Rgxs();
		String JSESSIONID=r.getJSESSIONID();
		r.login(JSESSIONID);
		
	}
	public String getJSESSIONID(){
		String JSESSIONID;
		String StatusCode;
		String time;
		RestAssured ra_VerifyCode = new RestAssured();
		ra_VerifyCode.baseURI = "http://10.10.227.155";
		ra_VerifyCode.port = 8092;
		ra_VerifyCode.basePath = "/puhui-cas/login?service=http://10.10.180.37:9092/cas-login";
		String s="username=guanxin&password=123456&captcha=&lt=LT-91693-umbYEtmZoAIwfMz6DgJ4TYyMEdJuon-inside.puhuifinance.com%2Fpuhui-cas&execution=e1s1&_eventId=submit";
		Long start=System.currentTimeMillis();
		Response re2 = ra_VerifyCode.given().given().get();
		Long end=System.currentTimeMillis();
		JSESSIONID=re2.getHeader("Set-Cookie").split(";")[0];
		System.out.println(JSESSIONID);
		return JSESSIONID;
	}
	public String login(String JSESSIONID){
		//登陆http://10.10.227.155:8092/puhui-cas/login;jsessionid=CB6D5A088E0131A8D3DBB4C09CF537EB?service=http://10.10.180.37:9092/cas-login&locale=zh_CN 
		String StatusCode;
		String time;
		RestAssured ra_VerifyCode = new RestAssured();
		ra_VerifyCode.baseURI = "http://10.10.227.155";
		ra_VerifyCode.port = 8092;
		ra_VerifyCode.basePath = "/puhui-cas/login;"+JSESSIONID+"?service=http://10.10.180.37:9092/cas-login&locale=zh_CN";
		String Parameter="username=guanxin&password=123456&captcha=&lt=LT-95983-ZMh4JH2vw71sRMqERKcTOHGuiPDUtb-inside.puhuifinance.com%2Fpuhui-cas&execution=e1s1&_eventId=submit";
		Long start=System.currentTimeMillis();
		
		Response re2 = ra_VerifyCode.given().given().contentType(ContentType.URLENC).
				headers("Cookie", JSESSIONID,
					"Referer","http://10.10.227.155:8092/puhui-cas/login?service=http://10.10.180.37:9092/cas-login",
					"Origin","http://10.10.227.155:8092"
					).body(Parameter).when().post();
		Long end=System.currentTimeMillis();

		System.out.println("第二步完成");
		
		System.out.println(re2.getHeaders().toString());
		System.out.println(re2.getBody().asString());
		System.out.print("名称:"+Thread.currentThread().getName()+"状态："+re2.getStatusCode()+"耗时:毫秒");
		return JSESSIONID;
	}
}

