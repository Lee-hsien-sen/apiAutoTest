package test;

import static io.restassured.http.ContentType.JSON;
import java.io.File;
import java.util.HashMap;
import org.json.simple.JSONObject;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;

public class RequestDataUtils2 {

	//发送post请求前的准备
	private static RestAssured getRMEnv() {
		RestAssured	ra = new RestAssured();
			ra.config = RestAssuredConfig.newConfig().sslConfig(
					SSLConfig.sslConfig().allowAllHostnames());
			ra.baseURI = "http://ut1.zuul.pub.puhuifinance.com";
				ra.port = 8765;
			ra.basePath = "/bestbuy-app-server-cloud-server/api";
		return ra;
	}
	


	//发送post请求返回整个响应结果
	public static Response getPostResponse() {
		RequestDataUtils2 rdu=new RequestDataUtils2();
		Response re=null;
		String url="/v1/customer/photo/upload";
		try{
			File filen=new File("out/3.jpg");
			re=rdu.getRMEnv().given()
					.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtYWMiOiIxNDk5NDMwNzQwNzQzIiwidXNlcklkIjoyMjQxMCwic3ViIjoiMTU4MTEwMDM0MzEiLCJpc3MiOiJybS1hcHAtc2VydmVyIiwiaWF0IjoxNTAwMzY5MDE4fQ.nixejoF9AJCnBnj7JUkP9kcROWW3qnpP_yKUydJ0i-U")
					.multiPart("file", filen)
//					.queryParam("orderId",map.get("orderId")+"&photoType="+map.get("photoType")+"&photoLocation="+map.get("photoLocation"))
//					.pathParam("photoType",map.get("photoType"))
//					.pathParam("photoLocation",map.get("photoLocation"))
					.when()
					.post(url+"?photoType=1&photoLocation=2&orderId=38858")
			        .thenReturn();
			
//			File filen=new File("out/"+filename);
//			re=rdu.getRMEnv(tem).given().header("Authorization", ZY.ZY_Token.get("token")==null? "":ZY.ZY_Token.get("token"))
//					.when().multiPart("file", filen)
//					.params(map)
//					.post(serviceURL).andReturn();
		}catch(Exception e){
			System.out.println(e.getMessage());
//			re=rdu.getRMEnv(tem).given().header("Authorization", ZY.ZY_Token.get("token")==null? "":ZY.ZY_Token.get("token"))
//					.when()
//					.params(map)
//					.post(serviceURL).andReturn();
		}
		System.out.println(re.asString());
		return null;
	}
	public static void main(String[] args) throws Exception {

		getPostResponse();
		
	}

}
