package com.dynamia.cms.site.payment;

import java.util.HashMap;
import java.util.Map;

public class PaymentForm {

	private String url;
	private String httpMethod;
	private Map<String, String> parameters = new HashMap<>();

	public void addParam(String name, String value) {
		parameters.put(name, value);
	}

	public void addParams(Map<String, String> params) {
		parameters.putAll(params);
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

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

}
