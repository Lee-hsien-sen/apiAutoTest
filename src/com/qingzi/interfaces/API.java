package com.qingzi.interfaces;

import io.restassured.response.Response;

import java.util.HashMap;

public interface API {
	//初始化方法
	void initialize(HashMap<String, Object> data);

	//输入前处理数据，返回处理结果
	HashMap<String, Object> handleInput(HashMap<String, Object> data);

	//发送请求的方法
	Response SendRequest(HashMap<String, String> headers,HashMap<String, Object> data,String Url,String Request);

	//输出结果前处理数据，返回Pass或者Fail+信息
	String handleOutput(Response re, HashMap<String, Object> data);

}
