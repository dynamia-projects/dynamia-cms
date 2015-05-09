package com.dynamia.cms.site.payment;

import java.util.HashMap;
import java.util.Map;

public class PaymentForm {

	private String url;
	private String httpMethod;
	private Map<String, String> paramaters = new HashMap<>();

	public void addParam(String name, String value) {
		paramaters.put(name, value);
	}

	public void addParams(Map<String, String> params) {
		paramaters.putAll(params);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public Map<String, String> getParamaters() {
		return paramaters;
	}

	public void setParamaters(Map<String, String> paramaters) {
		this.paramaters = paramaters;
	}

}
