package com.qingzi.testUtil;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;







import com.qingzi.process.BasicsGM;
import com.qingzi.process.QZ;
import com.qingzi.system.MyRequest;
import com.qingzi.system.system;

import static io.restassured.http.ContentType.JSON;


public class RequestDataUtils {
	
	//发送请求前的准备
	private RestAssured getRMEnv(system tem) {
		RestAssured	ra = new RestAssured();

			ra.config = RestAssuredConfig.newConfig().sslConfig(
					SSLConfig.sslConfig().allowAllHostnames());
//			System.out.println(""+tem.getRM_URI()+"");
			if(tem !=null){
				ra.baseURI = tem.getRM_URI();
				
//			System.out.println(tem.getRM_port());
				if (tem.getRM_port()!=null && tem.getRM_port().length()>1){
					ra.port = Integer.valueOf(tem.getRM_port());
				}
				
//			System.out.println(tem.getRM_basePath());
				if (tem.getRM_basePath()!=null && tem.getRM_basePath().length()>1){
					ra.basePath = tem.getRM_basePath();
				}
//				RestAssured.config = ra.config().connectionConfig(RestAssured.config().getConnectionConfig().closeIdleConnectionsAfterEachResponse());
			}
//			ra.config = RestAssured.config().redirect(RedirectConfig.redirectConfig().followRedirects(false));
			RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("x-www-form-urlencoded", ContentType.ANY).defaultContentCharset("utf-8"));
		return ra;
	}
	
	public static Response RestAssuredApi(HashMap<String, Object> data,MyRequest myRequest){
		if("get".equals(myRequest.getRequest())){
			return Get(data,myRequest);
		}else if("post".equals(myRequest.getRequest())){
			return PostAll(data,myRequest);
		}else if("put".equals(myRequest.getRequest())){
			return Put(data,myRequest);
		}else if("del".equals(myRequest.getRequest())){
			return Del(data,myRequest);
		}else{
			return null;
		}
		
	}
	
	public static Response PostAll(HashMap<String, Object> data,MyRequest myRequest){
		if(myRequest.getFormParameter()!=null){
			if(myRequest.getFile()!=null){
				return Post_file(data,myRequest);
			}else{
				return Post_form_data(data,myRequest);
			}
		}else if(myRequest.getParameter()!=null){
			return Post_JSON(data,myRequest);
		}
		return Post(data,myRequest);
	}
	
	public static RequestSpecification jobApi(HashMap<String, Object> data,MyRequest myRequest){
		String Parameter = (String) data.get("parameter");
		String system = MapUtil.getValue("system", data);
		system = replaceString(system);
		com.qingzi.system.system tem = (com.qingzi.system.system) (BasicsGM.map.get(system));
		
		RequestDataUtils rdu = new RequestDataUtils();
		String path="";
		if(myRequest.getUrl().startsWith("http://")){
			tem=null;
			path=myRequest.getUrl();
		}else{
			path=tem.getRM_URI()+(tem.getRM_port().equals("")?"":":"+tem.getRM_port())+tem.getRM_basePath()+myRequest.getUrl();
		}
		@SuppressWarnings("static-access")
		RequestSpecification  rsf=rdu.getRMEnv(tem).given();
		
		if(myRequest.getFormParameter()!=null){
			Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString()+"," +path + ",FormParameter="+myRequest.getFormParameter());
		}else{
			Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString()+"," +path + ",Parameter=" + Parameter);
		}
		
		if(myRequest.getHeaders()!=null){
			rsf=rsf.headers(myRequest.getHeaders());
		}
		if(myRequest.getCookies()!=null){
			rsf=rsf.cookies(myRequest.getCookies());
		}
		return rsf;
		
	}
	
	public static Response Post_file(HashMap<String, Object> data,MyRequest myRequest) {
		int state = 0;
		Response re = null;
		while (state < 5) {
			try {
				state++;
				File filen=new File("out/"+myRequest.getFile());
				re = jobApi(data,myRequest)
						.params(myRequest.getFormParameter()).when().multiPart("image", filen)
						.post(myRequest.getUrl()).thenReturn();
				if (!"200".equals(re.getStatusCode()+"")) {
					Log.logError("请求返回:" +re.getStatusCode()+",第"+ state + "次。");
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
	
	public static Response Post_form_data(HashMap<String, Object> data,MyRequest myRequest) {
			// 设置参数格式
			int state = 0;
			Response re = null;
			
			while (state < 5) {
				try {
					state++;
					re = jobApi(data,myRequest)
							.params(myRequest.getFormParameter())
							.when()
							.post(myRequest.getUrl())
					        .thenReturn();
					if (!"200".equals(re.getStatusCode()+"")) {
						Log.logError("请求返回" + re.getStatusCode() + "次。");
//					} else {
					}
					return re;
				} catch (Exception e) {
					e.printStackTrace();
					Log.logError("请求超时," + state + "次。");
				}
			}
			return re;
		}
	
	//不带token的post请求
	@SuppressWarnings("static-access")
	public static Response Post_JSON(HashMap<String, Object> data,MyRequest myRequest) {
			// 设置参数格式
		String Parameter = (String) data.get("parameter");
		int state = 0;
		Response re = null;
		while (state < 5) {
			try {
				state++;
				re = jobApi(data,myRequest)
						.contentType(JSON).body("{"+Parameter+"}").when()
						.post(myRequest.getUrl()).thenReturn();
				if (!"200".equals(re.getStatusCode()+"")) {
					Log.logError("请求返回," + re.getStatusCode() + "次。");
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
	
	public static Response Post(HashMap<String, Object> data,MyRequest myRequest) {
		// 设置参数格式
	String Parameter = (String) data.get("parameter");
	int state = 0;
	Response re = null;
	while (state < 5) {
		try {
			state++;
			re = jobApi(data,myRequest)
					.when()
					.post(myRequest.getUrl()).thenReturn();
			if (!"200".equals(re.getStatusCode()+"")) {
				Log.logError("请求返回," + re.getStatusCode() + "次。");
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
	
	
	@SuppressWarnings("deprecation")
	public static Response Put(HashMap<String, Object> data,MyRequest myRequest) {
		// 设置参数格式
		int state = 0;
		Response re = null;
		while (state < 5) {
			try {
				state++;
				re = jobApi(data,myRequest)
						.formParameters(myRequest.getFormParameter())
						.when().put(myRequest.getUrl()).thenReturn();
				JsonPath jp = re.body().jsonPath();
				if ("500".equals(jp.getString("status"))) {
					Log.logError("请求返回500," + state + "次。");
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
	
	public static Response Del(HashMap<String, Object> data,MyRequest myRequest) {
		int state = 0;
		Response re = null;
		while (state < 5) {
			try {
				state++;
				re =jobApi(data,myRequest)
						.when().delete(myRequest.getUrl()).thenReturn();
				JsonPath jp = re.body().jsonPath();
				if ("500".equals(jp.getString("status"))) {
					Log.logError("请求返回500," + state + "次。");
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
	
    public static Response Get(HashMap<String, Object> data,MyRequest myRequest) {
        //设置参数格式
		 String parameter = (String) data.get("parameter");
        int state=0;
        Response re=null;
        while(state<3){
            try {
                state++;
//                System.out.println(parameter != null);
//                System.out.println(!"".equals(parameter));
                if(parameter != null && !"".equals(parameter)) {
					re = jobApi(data,myRequest)
							.get(myRequest.getUrl() + "?" + parameter).thenReturn();
				}else {
					re = jobApi(data,myRequest)
							.get(myRequest.getUrl()).thenReturn();
					
				}
                if(!"200".equals(re.statusCode()+"")){
                    Log.logError("请求返回"+re.statusCode()+"第"+state+"次。");
                }else {
					return re;
				}
            } catch (Exception e) {
            	e.printStackTrace();
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
	
    public static Response Get_houtai(HashMap<String, Object> data,MyRequest myRequest) {
        //设置参数格式
		 String parameter = (String) data.get("parameter");
        int state=0;
        Response re=null;
        while(state<3){
            try {
                state++;
				re = jobApi(data,myRequest).config(RestAssured.config().redirect(RedirectConfig.redirectConfig().followRedirects(false)))
							.get(myRequest.getUrl()).thenReturn();
				
                if(!"307".equals(re.statusCode()+"")){
                    Log.logError("请求返回"+re.statusCode()+"次。");
                }else {
					return re;
				}
            } catch (Exception e) {
            	e.printStackTrace();
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
	 
    
    public static Response POST_houtai(HashMap<String, Object> data,MyRequest myRequest) {
        //设置参数格式
		 String parameter = (String) data.get("parameter");
        int state=0;
        Response re=null;
        while(state<3){
            try {
                state++;
				re = jobApi(data,myRequest)
							.post(myRequest.getUrl()).thenReturn();
				
                if(!"307".equals(re.statusCode()+"")){
                    Log.logError("请求返回"+re.statusCode()+"次。");
                }else {
					return re;
				}
            } catch (Exception e) {
            	e.printStackTrace();
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
	 
	 
	//不带token的post请求
		public static Response Post_headers(HashMap<String, Object> data, String serviceURL,String zgl_clienttype) {
				// 设置参数格式
			String Parameter = (String) data.get("parameter");

			String system = MapUtil.getValue("system", data);system = replaceString(system);
			com.qingzi.system.system tem = (com.qingzi.system.system) (BasicsGM.map.get(system));

			Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString() + ",Parameter=" + Parameter);

			int state = 0;
			Response re = null;
			RequestDataUtils rdu = new RequestDataUtils();
			while (state < 5) {
				try {
					state++;
					re = rdu.getRMEnv(tem).given()
							.header("zgl-systemtype", "Windows")
							.header("zgl-clienttype", zgl_clienttype)
							.contentType(JSON).body("{"+Parameter+"}").when()
							.post(serviceURL).thenReturn();
//						System.out.println(re.getStatusCode()+"............................");
//						System.out.println(re.asString());
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
	
	//不带token的post请求
	public static Response Post_NOToken(HashMap<String, Object> data, String serviceURL) {
			// 设置参数格式
		String Parameter = (String) data.get("parameter");

		String system = MapUtil.getValue("system", data);system = replaceString(system);
		com.qingzi.system.system tem = (com.qingzi.system.system) (BasicsGM.map.get(system));

		Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString() + ",Parameter=" + Parameter);

		int state = 0;
		Response re = null;
		RequestDataUtils rdu = new RequestDataUtils();
		while (state < 5) {
			try {
				state++;
				if("/web/member".equals(serviceURL)){
//					Cookie cookie1 = Cookie.Builder("username", "John").setComment("comment 1").build();
//					Cookie cookie2 = Cookie.Builder("token", 1234).setComment("comment 2").build();
//					Cookies cookies = new Cookies(cookie1, cookie2);
//					System.out.println(TEST.laravel_session);
//					System.out.println(TEST.XSRF_token);
					re = rdu.getRMEnv(tem).given()
//							.header("laravel_session", XYZB.laravel_session)
//							.header("XSRF-TOKEN", XYZB.XSRF_token)
//							.cookie("laravel_session", XYZB.laravel_session)
//							.cookie("XSRF-TOKEN", XYZB.XSRF_token)
							.contentType(JSON).body("{"+Parameter+"}").when()
							.post(serviceURL).thenReturn();
//					System.out.println(re.getStatusCode()+"............................");
//					System.out.println(re.asString());
//					Headers headers=re.getHeaders();
//					System.out.println(headers.toString());
//							System.out.println(re.getSessionId());
							
//					JsonPath jp = re.body().jsonPath();
					if (!"200".equals(re.getStatusCode()+"")) {
						Log.logError("请求返回:" +re.getStatusCode()+",第"+ state + "次。");
						Thread.sleep(1000);
					} else {
						return re;
					}
				}else{
					re = rdu.getRMEnv(tem).given()
//							.header("laravel_session", XYZB.laravel_session)
//							.cookie("laravel_session", XYZB.laravel_session)
							.contentType(JSON).body("{"+Parameter+"}").when()
							.post(serviceURL).thenReturn();
//					System.out.println(re.getStatusCode()+"............................");
//					System.out.println(re.asString());
					JsonPath jp = re.body().jsonPath();
					if ("500".equals(jp.getString("status"))) {
						Log.logError("请求返回500," + state + "次。");
						Thread.sleep(1000);
					} else {
						return re;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.logError("请求超时," + state + "次。");
			}
		}
		return re;
	}
	
	//不带token的post请求
		@SuppressWarnings("static-access")
		public static Response Post_NOtokenNOcookie(HashMap<String, Object> data, String serviceURL) {
				// 设置参数格式
			String Parameter = (String) data.get("parameter");

			String system = MapUtil.getValue("system", data);system = replaceString(system);
			com.qingzi.system.system tem = (com.qingzi.system.system) (BasicsGM.map.get(system));

			Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString() + ",Parameter=" + Parameter);

			int state = 0;
			Response re = null;
			RequestDataUtils rdu = new RequestDataUtils();
			while (state < 5) {
				try {
					state++;
					re = rdu.getRMEnv(tem).given()
							.contentType(JSON).body("{"+Parameter+"}").when()
							.post(serviceURL).thenReturn();
					if (!"200".equals(re.getStatusCode()+"")) {
						Log.logError("请求返回," + re.getStatusCode() + "次。");
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
	
	//需要添加token的post，json请求
	@SuppressWarnings("static-access")
	public static Response Post_token(HashMap<String, Object> data, String serviceURL, String token) {
//		// 设置参数格式
//		String Parameter = (String) data.get("parameter");
//
//		String system = MapUtil.getValue("system", data);system = replaceString(system);
//		com.offcn.system.system tem = (com.offcn.system.system) (BasicsGM.map.get(system));
//
//		Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString() + ",Parameter=" + Parameter);
//
//		int state = 0;
//		Response re = null;
//		RequestDataUtils rdu = new RequestDataUtils();
//		while (state < 5) {
//			try {
//				state++;
////				re = rdu.getRMEnv(tem).given().cookie("laravel_session", XYZB.laravel_session).when().get("http://live.offcncloud.com/web/admin").thenReturn();
//				re = rdu.getRMEnv(tem).given().header("Authorization",token)
//						.contentType(JSON).body("{"+Parameter+"}").when()
//						.post(serviceURL).thenReturn();
////				System.out.println(re.asString());
//				if (!"200".equals(re.getStatusCode()+"")) {
//					Log.logError("请求返回:" +re.getStatusCode()+",第"+ state + "次。");
//					Thread.sleep(1000);
//				} else {
//					return re;
//				}
//			} catch (Exception e) {
//				Log.logError("请求超时," + state + "次。");
//			}
//		}
//		return re;
	// 设置参数格式
			String Parameter = (String) data.get("parameter");
	
			String system = MapUtil.getValue("system", data);
			com.qingzi.system.system tem = (com.qingzi.system.system) (BasicsGM.map.get(system));
	
			Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString() + ",Parameter=" + Parameter);
	
			int state = 0;
			Response re = null;
			RequestDataUtils rdu = new RequestDataUtils();
			while (state < 5) {
				try {
					state++;
	//						re = rdu.getRMEnv(tem).given().cookie("laravel_session", XYZB.laravel_session).when().get("http://live.offcncloud.com/web/admin").thenReturn();
					re = rdu.getRMEnv(tem).given().header("Authorization","Bearer "+ token)
							.contentType(JSON).body("{"+Parameter+"}").when()
							.post(serviceURL).thenReturn();
	//						System.out.println(MapUtil.showMap_String(re.getCookies()));
					if (!"200".equals(re.getStatusCode()+"")) {
						Log.logError("请求返回:" +re.getStatusCode()+",第"+ state + "次。");
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
	
	//需要添加token的post，json请求
	@SuppressWarnings("static-access")
	public static Response Post_cooike(HashMap<String, Object> data, String serviceURL, String cooikeName, String cooikevalue) {
		// 设置参数格式
		String Parameter = (String) data.get("parameter");

		String system = MapUtil.getValue("system", data);system = replaceString(system);
		com.qingzi.system.system tem = (com.qingzi.system.system) (BasicsGM.map.get(system));

		Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString() + ",Parameter=" + Parameter);

		int state = 0;
		Response re = null;
		RequestDataUtils rdu = new RequestDataUtils();
		while (state < 5) {
			try {
				state++;
//					re = rdu.getRMEnv(tem).given().cookie("laravel_session", XYZB.laravel_session).when().get("http://live.offcncloud.com/web/admin").thenReturn();
				re = rdu.getRMEnv(tem).given().cookie(cooikeName, cooikevalue)
						.contentType(JSON).body("{"+Parameter+"}").when()
						.post(serviceURL).thenReturn();
//					System.out.println(re.asString());
				if (!"200".equals(re.getStatusCode()+"")) {
					Log.logError("请求返回:" +re.getStatusCode()+",第"+ state + "次。");
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
	
	//需要添加token的post，json请求
	@SuppressWarnings("static-access")
	public static Response Post_cooike_form_data(HashMap<String, Object> data, 
			String serviceURL, String cooikeName, String cooikevalue,Map<String, String>  jsonAsMap) {
		// 设置参数格式
		String Parameter = (String) data.get("parameter");

		String system = MapUtil.getValue("system", data);system = replaceString(system);
		com.qingzi.system.system tem = (com.qingzi.system.system) (BasicsGM.map.get(system));

		Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString() + ",Parameter=" + Parameter);

		int state = 0;
		Response re = null;
		RequestDataUtils rdu = new RequestDataUtils();
		while (state < 5) {
			try {
				state++;
//						re = rdu.getRMEnv(tem).given().cookie("laravel_session", XYZB.laravel_session).when().get("http://live.offcncloud.com/web/admin").thenReturn();
				re = rdu.getRMEnv(tem).given().cookie(cooikeName, cooikevalue)
						.params(jsonAsMap)
						.when()
						.post(serviceURL).thenReturn();
//						System.out.println(re.asString());
				if (!"200".equals(re.getStatusCode()+"")) {
					Log.logError("请求返回:" +re.getStatusCode()+",第"+ state + "次。");
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
	
	//需要添加token的post，json请求
	@SuppressWarnings("static-access")
	public static Response Post_cooike2_form_data(HashMap<String, Object> data, 
			String serviceURL,Map<String, String>  jsonAsMap, String cooikeName, String cooikevalue, String cooikeName2, String cooikevalue2) {
		// 设置参数格式
		String Parameter = (String) data.get("parameter");

		String system = MapUtil.getValue("system", data);system = replaceString(system);
		com.qingzi.system.system tem = (com.qingzi.system.system) (BasicsGM.map.get(system));

		Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString() + ",Parameter=" + Parameter);

		int state = 0;
		Response re = null;
		RequestDataUtils rdu = new RequestDataUtils();
		while (state < 5) {
			try {
				state++;
						System.out.println(tem.getRM_URI()+tem.getRM_port()+tem.getRM_basePath()+serviceURL);
						if("".equals(cooikevalue) && "".equals(cooikevalue2)){
							re = rdu.getRMEnv(tem).given()
									.params(jsonAsMap)
									.when()
									.post(serviceURL).thenReturn();
						}else{
							re = rdu.getRMEnv(tem).given().cookie(cooikeName, cooikevalue).cookie(cooikeName2, cooikevalue2)
									.params(jsonAsMap)
									.when()
									.post(serviceURL).thenReturn();
						}
//							System.out.println(re.asString());
				if (!"200".equals(re.getStatusCode()+"")) {
					Log.logError("请求返回:" +re.getStatusCode()+",第"+ state + "次。");
				} else {
					return re;
				}
			} catch (Exception e) {
				Log.logError("请求超时," + state + "次。");
			}
		}
		return re;
	}
	
	
	//需要添加token的post，json请求
	@SuppressWarnings("static-access")
	public static Response Post_file(HashMap<String, Object> data, String serviceURL, 
			String cooikeName, String cooikevalue,String file,String filename) {
		// 设置参数格式
		String Parameter = (String) data.get("parameter");

		String system = MapUtil.getValue("system", data);system = replaceString(system);
		com.qingzi.system.system tem = (com.qingzi.system.system) (BasicsGM.map.get(system));

		Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString() + ",Parameter=" + Parameter);

		int state = 0;
		Response re = null;
		RequestDataUtils rdu = new RequestDataUtils();
		while (state < 5) {
			try {
				state++;
				File filen=new File("out/"+filename);
//						re = rdu.getRMEnv(tem).given().cookie("laravel_session", XYZB.laravel_session).when().get("http://live.offcncloud.com/web/admin").thenReturn();
				re = rdu.getRMEnv(tem).given().cookie(cooikeName, cooikevalue)
						.body("{"+Parameter+"}").when().multiPart(file, filen)
						.post(serviceURL).thenReturn();
//						System.out.println(re.asString());
				if (!"200".equals(re.getStatusCode()+"")) {
					Log.logError("请求返回:" +re.getStatusCode()+",第"+ state + "次。");
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
	
	//需要添加token的post，json请求
	@SuppressWarnings("static-access")
	public static Response Post_file2(HashMap<String, Object> data, String serviceURL, 
			String token,String file,String filename,Map<String, String>  jsonAsMap) {
		// 设置参数格式
		String Parameter = (String) data.get("parameter");

		String system = MapUtil.getValue("system", data);system = replaceString(system);
		com.qingzi.system.system tem = (com.qingzi.system.system) (BasicsGM.map.get(system));

		Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString() + ",Parameter=" + Parameter);

		int state = 0;
		Response re = null;
		RequestDataUtils rdu = new RequestDataUtils();
		while (state < 5) {
			try {
				state++;
				File filen=new File("out/"+filename);
//							re = rdu.getRMEnv(tem).given().cookie("laravel_session", XYZB.laravel_session).when().get("http://live.offcncloud.com/web/admin").thenReturn();
				re = rdu.getRMEnv(tem).given()
						.header("Authorization",token)
						.params(jsonAsMap).when().multiPart(file, filen)
						.post(serviceURL).thenReturn();
//							System.out.println(re.asString());
				if (!"200".equals(re.getStatusCode()+"")) {
					Log.logError("请求返回:" +re.getStatusCode()+",第"+ state + "次。");
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
	
	//发送get请求返回整个响应结果
    @SuppressWarnings("static-access")
    public static Response Get_Noparameter(HashMap<String, Object> data,
                                          String serviceURL,String token) {
        //设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);

        int state=0;
        Response re=null;
        RequestDataUtils rdu=new RequestDataUtils();
        while(state<3){
            try {
                state++;
                 
                if(parameter != null && !"".equals(parameter)) {
					re = rdu.getRMEnv(tem).given().header("Authorization","Bearer "+ token).get(serviceURL + "/" + parameter).andReturn();
				}else{
					re = rdu.getRMEnv(tem).given().header("Authorization","Bearer "+ token).get(serviceURL).andReturn();
				}
                if(!"200".equals(re.statusCode()+"")){
                    Log.logError("请求返回"+re.statusCode()+"次。");
                }else{
                    return re;
                }
            } catch (Exception e) {
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
	
  //发送get请求返回整个响应结果
    @SuppressWarnings("static-access")
    public static Response Get_token_all(HashMap<String, Object> data,
                                          String serviceURL,String token) {
        //设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);

        int state=0;
        Response re=null;
        RequestDataUtils rdu=new RequestDataUtils();
        while(state<3){
            try {
                state++;
                 
                if(parameter != null && !"".equals(parameter)) {
					re = rdu.getRMEnv(tem).given().header("Authorization",token).get(serviceURL + "?" + parameter).andReturn();
				}else {
					re = rdu.getRMEnv(tem).given().header("Authorization",token).get(serviceURL).andReturn();
				}
                if(!"200".equals(re.statusCode()+"")){
                    Log.logError("请求返回"+re.statusCode()+"次。");
                }else{
                    return re;
                }
            } catch (Exception e) {
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
	
	//发送get请求返回整个响应结果
    @SuppressWarnings("static-access")
    public static Response Get_token(HashMap<String, Object> data,
                                          String serviceURL,String token) {
        //设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);

        int state=0;
        Response re=null;
        RequestDataUtils rdu=new RequestDataUtils();
        while(state<3){
            try {
                state++;
                 
                if(parameter != null && !"".equals(parameter)) {
					re = rdu.getRMEnv(tem).given().header("Authorization","Bearer "+ token).get(serviceURL + "?" + parameter).andReturn();
				}else {
					re = rdu.getRMEnv(tem).given().header("Authorization","Bearer "+ token).get(serviceURL).andReturn();
				}
                if(!"200".equals(re.statusCode()+"")){
                    Log.logError("请求返回"+re.statusCode()+"次。");
                }else{
                    return re;
                }
            } catch (Exception e) {
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
	
  //get错误token请求
    @SuppressWarnings("static-access")
    public static Response Get_Errtoken(HashMap<String, Object> data,
                                          String serviceURL,String token) {
        //设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);

        int state=0;
        Response re=null;
        RequestDataUtils rdu=new RequestDataUtils();
        while(state<5){
            try {
                state++;
                 
                if(parameter != null && !"".equals(parameter)) {
					re = rdu.getRMEnv(tem).given().header("Authorization","Bearer "+  token+"1").get(serviceURL + "/" + parameter).andReturn();
				}else {
					re = rdu.getRMEnv(tem).given().header("Authorization","Bearer "+  token+"1").get(serviceURL).andReturn();
				}
                if("500".equals(re.statusCode()+"")){
                    Log.logError("请求返回500,"+state+"次。");
                }else{
                    return re;
                }
            } catch (Exception e) {
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
  	
  //get超长token请求
    @SuppressWarnings("static-access")
    public static Response Get_Longtoken(HashMap<String, Object> data,
                                          String serviceURL,String token) {
        //设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);

        int state=0;
        Response re=null;
        RequestDataUtils rdu=new RequestDataUtils();
        while(state<5){
            try {
                state++;
                 
                if(parameter != null && !"".equals(parameter)) {
					re = rdu.getRMEnv(tem).given().header("Authorization","Bearer "+  token+token+token+token+token).get(serviceURL + "/" + parameter).andReturn();
				}else {
					re = rdu.getRMEnv(tem).given().header("Authorization", "Bearer "+ token+token+token+token+token).get(serviceURL).andReturn();
				}
                if("500".equals(re.statusCode()+"")){
                    Log.logError("请求返回500,"+state+"次。");
                }else{
                    return re;
                }
            } catch (Exception e) {
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
    
  //get无token请求
    @SuppressWarnings("static-access")
    public static Response Get_Notoken(HashMap<String, Object> data,
                                          String serviceURL) {
        //设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);

        int state=0;
        Response re=null;
        RequestDataUtils rdu=new RequestDataUtils();
        while(state<5){
            try {
                state++;
				re = rdu.getRMEnv(tem).given().cookie("laravel_session",QZ.laravel_session).get(serviceURL).andReturn();
                
//				System.out.println(re.asString());
				if(!"200".equals(re.statusCode()+"")){
                    Log.logError("请求返回,"+re.statusCode()+"。");
                }else{
                    return re;
                }
            } catch (Exception e) {
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
    
  //get无token请求
    @SuppressWarnings("static-access")
    public static Response Get_Notoken_NoCookie(HashMap<String, Object> data,
                                          String serviceURL) {
        //设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);

        int state=0;
        Response re=null;
        RequestDataUtils rdu=new RequestDataUtils();
        while(state<5){
            try {
                state++;
                System.out.println(tem.getRM_URI()+serviceURL);
				re = rdu.getRMEnv(null).given().get(tem.getRM_URI()+serviceURL).andReturn();
                
//				System.out.println(re.asString());
				if(!"200".equals(re.statusCode()+"")){
                    Log.logError("请求返回,"+re.statusCode()+"。");
                }else{
                    return re;
                }
            } catch (Exception e) {
            	e.printStackTrace();
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
    
    @SuppressWarnings("static-access")
    public static Response Get_cookie(HashMap<String, Object> data,
                                          String serviceURL) {
        //设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);

        int state=0;
        Response re=null;
        RequestDataUtils rdu=new RequestDataUtils();
        while(state<5){
            try {
                state++;
				re = rdu.getRMEnv(tem).given()
						.cookie("laravel_session",QZ.laravel_session)
						.cookie("XSRF-TOKEN", QZ.XSRF_token)
						.get(serviceURL).andReturn();
                if(!"200".equals(re.statusCode()+"")){
                    Log.logError("请求返回,"+re.statusCode()+"。");
                }else{
                    return re;
                }
            } catch (Exception e) {
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
    
    public static void main(String[] args) {
    	Get_two_cookie(null,"","","","","");
	}
    
    @SuppressWarnings("static-access")
    public static Response Get_two_cookie(HashMap<String, Object> data,
                                          String serviceURL,
                                          String cookie1Name,
                                          String cookie1value,
                                          String cookie2Name,
                                          String cookie2value
                                          ) {
        //设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);

        int state=0;
        Response re=null;
        RequestDataUtils rdu=new RequestDataUtils();
        System.out.println(tem.getRM_URI()+tem.getRM_port()+tem.getRM_basePath()+serviceURL);
        while(state<5){
            try {
                state++;
				re = rdu.getRMEnv(tem).given()
						.cookie(cookie1Name,cookie1value)
						.cookie(cookie2Name, cookie2value)
						.get(serviceURL).andReturn();
				System.out.println(re.asString());
                if(!"200".equals(re.statusCode()+"")){
                    Log.logError("请求返回,"+re.statusCode()+"。");
                }else{
                    return re;
                }
            } catch (Exception e) {
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
    
    @SuppressWarnings("static-access")
    public static Response Get_one_cookie(HashMap<String, Object> data,
                                          String serviceURL,
                                          String cookie1Name,
                                          String cookie1value
                                          ) {
        //设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);

        int state=0;
        Response re=null;
        RequestDataUtils rdu=new RequestDataUtils();
        if(serviceURL.contains("http:")){
        	tem=null;
        }
        while(state<5){
            try {
                state++;
				re = rdu.getRMEnv(tem).given()
						.cookie(cookie1Name,cookie1value)
						.get(serviceURL).andReturn();
                if(!"200".equals(re.statusCode()+"")){
                    Log.logError("请求返回,"+re.statusCode()+"。");
                }else{
                    return re;
                }
            } catch (Exception e) {
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
    
    
    @SuppressWarnings("static-access")
    public static Response Get_one_cookie_pre(HashMap<String, Object> data,
                                          String serviceURL,
                                          String cookie1Name,
                                          String cookie1value
                                          ) {
        //设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);

        int state=0;
        Response re=null;
        RequestDataUtils rdu=new RequestDataUtils();
        while(state<5){
            try {
                state++;
                if(parameter != null && !"".equals(parameter)) {
					re = rdu.getRMEnv(tem).given()
							.cookie(cookie1Name,cookie1value)
							.get(serviceURL+"?"+parameter).andReturn();
                }else {
					re = rdu.getRMEnv(tem).given()
							.cookie(cookie1Name,cookie1value)
							.get(serviceURL).andReturn();
				}
                if(!"200".equals(re.statusCode()+"")){
                    Log.logError("请求返回,"+re.statusCode()+"。");
                }else{
                    return re;
                }
            } catch (Exception e) {
                Log.logError("请求超时,"+state+"次。");
            }
        }
        return re;
    }
    
    
  //带token的pout请求
  	@SuppressWarnings("deprecation")
	public static Response Put_Token(HashMap<String, Object> data,String token, String serviceURL,Map<String, Object>  jsonAsMap) {

  	//设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);
		// 设置参数格式
		int state = 0;
		Response re = null;
		RequestDataUtils rdu = new RequestDataUtils();
		while (state < 5) {
			try {
				state++;
				re = rdu.getRMEnv(tem).given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs("x-www-form-urlencoded", ContentType.ANY)
								.defaultContentCharset("utf-8")))
						.header("Authorization","Bearer "+ token)
								.formParameters(jsonAsMap).
//						.formParam("start_time", "2018-08-31 22:19:42")
//							.formParam("end_time", "2018-08-31 23:00:00")
//							.formParam("room_name", "呜呜呜呜22s11")
//							.formParam("office_type", "国家公务员")
//							.formParam("province", "北京")
//							.formParam("city", "市辖区")
//							.formParam("area", "东城区").
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
  	public static Response Del_Token(HashMap<String, Object> data,String token, String serviceURL) {
  	//设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);
  			// 设置参数格式
  			Log.logInfo("serviceURL=" + serviceURL + ",Parameter=" + token);
  			int state = 0;
  			Response re = null;
  			RequestDataUtils rdu = new RequestDataUtils();
  			while (state < 5) {
  				try {
  					state++;
  					re = rdu.getRMEnv(tem).given().header("Authorization","Bearer "+ token).
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
  //带token的pout请求
  	@SuppressWarnings("deprecation")
	public static Response post_form_data(HashMap<String, Object> data,String token, String serviceURL,Map<String, String>  jsonAsMap) {

  	//设置参数格式
        String parameter= MapUtil.getParameter(data);

        String system= MapUtil.getValue("system", data);system = replaceString(system);
        com.qingzi.system.system tem=(com.qingzi.system.system)(BasicsGM.map.get(system));


		parameter=parameter.substring(1, parameter.length()-1);
        Log.logInfo(data.get("Description").toString()+","+data.get("TCNO").toString()+",Parameter="+parameter);
		// 设置参数格式
		int state = 0;
		Response re = null;
		RequestDataUtils rdu = new RequestDataUtils();
		while (state < 5) {
			try {
				state++;
				re = rdu.getRMEnv(tem).given().header("Authorization",token)
						.params(jsonAsMap)
						.when()
						.post(serviceURL)
				        .thenReturn();
				
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
  	
  //不带token的post请求
  		public static Response Post_headers_token(HashMap<String, Object> data, String serviceURL,String zgl_clienttype,String Salesman_token) {
  				// 设置参数格式
  			String Parameter = (String) data.get("parameter");

  			String system = MapUtil.getValue("system", data);
  			com.qingzi.system.system tem = (com.qingzi.system.system) (BasicsGM.map.get(system));

  			Log.logInfo(data.get("Description").toString() + "," + data.get("TCNO").toString() + ",Parameter=" + Parameter);

  			int state = 0;
  			Response re = null;
  			RequestDataUtils rdu = new RequestDataUtils();
  			while (state < 5) {
  				try {
  					state++;
  					re = rdu.getRMEnv(tem).given().header("Authorization","Bearer "+ Salesman_token)
  							.header("zgl-systemtype", "Windows")
  							.header("zgl-clienttype", zgl_clienttype)
  							.contentType(JSON).body("{"+Parameter+"}").when()
  							.post(serviceURL).thenReturn();
//  						System.out.println(re.getStatusCode()+"............................");
//  						System.out.println(re.asString());
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
  	public static String replaceString(String SystemName){
  		if(SystemName.contains("_")){
  			SystemName=SystemName.split("_")[0];
  		}
  		return SystemName;
  	}
}
