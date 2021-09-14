package com.qingzi.system;

import java.util.Map;

public class MyRequest {

	private String Request;
	private String Url;
	private Map<String, String> Cookies;
	private Map<String,String> headers;
	private String Parameter; //json请求方式的参数
	private Map<String, Object> formParameter;//form_data请求方式的参数、或file请求方式的参数
	private String file;
	public String getRequest() {
		return Request;
	}
	public void setRequest(String request) {
		Request = request;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public Map<String, String> getCookies() {
		return Cookies;
	}
	public void setCookies(Map<String, String> cookies) {
		Cookies = cookies;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public String getParameter() {
		return Parameter;
	}
	public void setParameter(String parameter) {
		Parameter = parameter;
	}
	public Map<String, Object> getFormParameter() {
		return formParameter;
	}
	public void setFormParameter(Map<String, Object> formParameter) {
		this.formParameter = formParameter;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public MyRequest(String request, String url, Map<String, String> cookies,
			Map<String, String> headers, String parameter,
			Map<String, Object> formParameter, String file) {
		super();
		Request = request;
		Url = url;
		Cookies = cookies;
		this.headers = headers;
		Parameter = parameter;
		this.formParameter = formParameter;
		this.file = file;
	}
	public MyRequest() {
	}
	
}
