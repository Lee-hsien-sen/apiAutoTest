package test;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.ArrayList;





import com.qingzi.testUtil.Log;
import com.qingzi.testUtil.StringUtils;

import static io.restassured.http.ContentType.JSON;


public class RequestDataUtils {
	
	public static void main(String[] args) {

		String token="eyJpdiI6IjhNYlJuUW1lWG1abFVsY1dlZFV3bGc9PSIsInZhbHVlIjoiMXJSVjR5U2pYQVwvV0tpK2FrdDZiT29yNTBqMEJpeHhmVk1NVkg0bmJva2Q4eklDZlZ3RllNTTlGMk4yb1wva1BTbTRURDZIWFFSQUJWSk5MaW1YaEQyQ2I3WlVaNXR3NGRUN3F5NmdtNnBFbldHdXV0b2hiMGVPaVJIOVpheFFteCIsIm1hYyI6IjkyNzkwNDc3MjNkMTRkYjA0MmJiZWEyZTc0ZGIwYmUzZTZlZjA2NjE0MGQwODg2YzQ3OWNjMmU2MjM3YzllNjcifQ==";
		String Parameter="{\"name\":111}";
		for(int i=0;i<1000;i++){
			Response re = Post_Token(token,Parameter);
			System.out.println(StringUtils.decodeUnicode(re.asString()));
		}
	}
	
	
	
	
	
	
	//发送请求前的准备
	private  RestAssured getRMEnv() {
		RestAssured	ra = new RestAssured();
			ra.config = RestAssuredConfig.newConfig().sslConfig(
					SSLConfig.sslConfig().allowAllHostnames());
//			System.out.println(tem.getRM_URI());http://student.eoffcn.com/ajax/jsonp/customer_service?phone=13366993819
//			ra.baseURI = "http://e.eoffcn.com";
//			ra.baseURI = "http://student.eoffcn.com";s
//			ra.urlEncodingEnabled=true;
//			System.out.println(Integer.valueOf(tem.getRM_port()));
//			if (!ReadProperties.GetPropertyByKey("isProduct").contains("Y")){
//				ra.port = 8765;
//			}
//			System.out.println(tem.getRM_basePath());
//			ra.basePath = "/api/course_query.php";
//			ra.basePath = "/ajax/jsonp/customer_service";
//			ra.urlEncodingEnabled =true;
//			RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("x-www-form-urlencoded", ContentType.ANY).defaultContentCharset("utf-8"));
		return ra;
	}
	
	//发送get请求返回整个响应结果
    @SuppressWarnings("static-access")
    public static Response Get_Noparameter(String token) {
        int state=0;
        Response re=null;
        RequestDataUtils rdu=new RequestDataUtils();
        Long sum=0L;
//        while(state<10){
            try {
                state++;
                Long startTime=System.currentTimeMillis();
	          	re = rdu.getRMEnv().given().header("Authorization","Bearer "+ token).get("http://live.offcncloud.com/api/v1/room_files").andReturn();
	          	Long endTime=System.currentTimeMillis();
//	          	String time=(endTime-startTime)+"毫秒";
	          	sum=sum+(endTime-startTime);
//                System.out.println(time+","+sum);
            } catch (Exception e) {
                Log.logError("请求超时,"+state+"次。");
            }
//        }
//        System.out.println(sum/10);
//        System.out.println(re.asString());
//        JsonPath jp = re.body().jsonPath();
		ArrayList<Object> list=(ArrayList<Object>) re.body().jsonPath().getList("data");
		System.out.println(list.size());
        return re;
    }
	
	
	public static Response Post_NOToken1(String Parameter,String token) {
		// 设置参数格式
		//	String Parameter = (String) data.get("parameter");
		//
		//	String system = MapUtil.getValue("system", data);
		//	com.offcn.system.system tem = (com.offcn.system.system) (BasicsGM.map.get(system));
		
		//	Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString() + ",Parameter=" + Parameter);
		
			int state = 0;
			Response re = null;
			RequestDataUtils rdu = new RequestDataUtils();
			while (state < 5) {
				try {
					state++;
		//				Cookie cookie1 = Cookie.Builder("username", "John").setComment("comment 1").build();
		//				Cookie cookie2 = Cookie.Builder("token", 1234).setComment("comment 2").build();
		//				Cookies cookies = new Cookies(cookie1, cookie2);
//						System.out.println(XYZB.laravel_session);
						System.out.println(Parameter);
						re = rdu.getRMEnv().given().cookie("laravel_session", token).contentType(JSON).body("{"+Parameter+"}").when()
								.post("/web/member").thenReturn();
						System.out.println(re.getStatusCode()+"............................");
						System.out.println(re.asString());
		//				JsonPath jp = re.body().jsonPath();
						if (!"200".equals(re.getStatusCode())) {
							Log.logError("请求返回:" +re.getStatusCode()+",第"+ state + "次。");
							Thread.sleep(1000);
						} else {
							return re;
						}
					
				} catch (Exception e) {
					e.printStackTrace();
					Log.logError("请求超时," + state + "次。");
				}
			}
			return re;
	}
	
	public static Response Post_NOToken2(String Parameter,String token) {
		// 设置参数格式
		//	String Parameter = (String) data.get("parameter");
		//
		//	String system = MapUtil.getValue("system", data);
		//	com.offcn.system.system tem = (com.offcn.system.system) (BasicsGM.map.get(system));
		
		//	Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString() + ",Parameter=" + Parameter);
		
			int state = 0;
			Response re = null;
			RequestDataUtils rdu = new RequestDataUtils();
			while (state < 5) {
				try {
					state++;
		//				Cookie cookie1 = Cookie.Builder("username", "John").setComment("comment 1").build();
		//				Cookie cookie2 = Cookie.Builder("token", 1234).setComment("comment 2").build();
		//				Cookies cookies = new Cookies(cookie1, cookie2);
//						System.out.println(XYZB.laravel_session);
						System.out.println(Parameter);
						re = rdu.getRMEnv().given().contentType(JSON).body("{"+Parameter+"}").when()
								.post("/web/login").thenReturn();
						System.out.println(re.getStatusCode()+"............................");
						System.out.println(re.asString());
		//				JsonPath jp = re.body().jsonPath();
						if (!"200".equals(re.getStatusCode())) {
							Log.logError("请求返回:" +re.getStatusCode()+",第"+ state + "次。");
							Thread.sleep(1000);
						} else {
							return re;
						}
					
				} catch (Exception e) {
					e.printStackTrace();
					Log.logError("请求超时," + state + "次。");
				}
			}
			return re;
	}

	//不带token的post请求
	public static Response Post_Token(String token, String Parameter) {
			// 设置参数格式
			int state = 0;
			Response re = null;
			RequestDataUtils rdu = new RequestDataUtils();
			while (state < 5) {
				try {
					state++;
					re = rdu.getRMEnv().given()
							.header("Authorization","Bearer "+ token)
							.contentType(JSON)
							.body(Parameter).when()
							.post("http://live.offcncloud.com/api/v1/questions").thenReturn();
					JsonPath jp = re.body().jsonPath();
					if ("500".equals(jp.getString("status"))) {
						Log.logError("请求返回500," + state + "次。");
						Thread.sleep(1000);
					} else {
						return re;
					}
				} catch (Exception e) {
					Log.logError("请求超时," + state + "次。");
				}
			}
			return re;
		}
	
	//带token的pout请求
	public static Response Put_Token(String token, String serviceURL) {
//		Map<String, Object>  jsonAsMap = new HashMap<>();
//		jsonAsMap.put("start_time", "2018-08-29 14:16:42");
//		jsonAsMap.put("end_time", "2018-08-29 23:00:00");
//		jsonAsMap.put("room_name", "201808290008");
//		jsonAsMap.put("office_type", "国家公务员");
//		jsonAsMap.put("province", "北京");
//		jsonAsMap.put("city", "市辖区");
//		jsonAsMap.put("area", "东城区");
			// 设置参数格式
			Log.logInfo("serviceURL=" + serviceURL + ",Parameter=" + token);
			int state = 0;
			Response re = null;
			RequestDataUtils rdu = new RequestDataUtils();
			//.given().contentType("x-www-form-urlencoded; ContentType.TEXT)")
			while (state < 5) {
				try {
					state++;
					
					re = rdu.getRMEnv()
							.given().config(RestAssured.config()
									.encoderConfig(EncoderConfig.encoderConfig()
											.encodeContentTypeAs("x-www-form-urlencoded", ContentType.ANY)
											.defaultContentCharset("utf-8")))
							.header("Authorization","Bearer "+ token)
							.formParam("start_time", "2018-08-31 22:19:42")
							.formParam("end_time", "2018-08-31 23:00:00")
							.formParam("room_name", "呜呜呜呜22s11")
							.formParam("office_type", "国家公务员")
							.formParam("province", "北京")
							.formParam("city", "市辖区")
							.formParam("area", "东城区").
							when().put(serviceURL).thenReturn();
					JsonPath jp = re.body().jsonPath();
					if ("500".equals(jp.getString("status"))) {
						Log.logError("请求返回500," + state + "次。");
						Thread.sleep(1000);
					} else {
						return re;
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.logError("请求超时," + state + "次。");
				}
			}
			return re;
		}
	//带token的del请求
	public static Response Del_Token(String token, String serviceURL) {
			// 设置参数格式
			Log.logInfo("serviceURL=" + serviceURL + ",Parameter=" + token);
			int state = 0;
			Response re = null;
			RequestDataUtils rdu = new RequestDataUtils();
			while (state < 5) {
				try {
					state++;
					re = rdu.getRMEnv().given().header("Authorization","Bearer "+ token).
							when().delete(serviceURL).thenReturn();
					JsonPath jp = re.body().jsonPath();
					if ("500".equals(jp.getString("status"))) {
						Log.logError("请求返回500," + state + "次。");
						Thread.sleep(1000);
					} else {
						return re;
					}
				} catch (Exception e) {
					Log.logError("请求超时," + state + "次。");
				}
			}
			return re;
		}
}
