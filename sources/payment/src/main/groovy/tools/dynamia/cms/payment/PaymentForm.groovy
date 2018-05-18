/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.payment

class PaymentForm {

	private String url
    private String httpMethod
    private Map<String, String> parameters = new HashMap<>()

    void addParam(String name, String value) {
		parameters.put(name, value)
    }

    void addParams(Map<String, String> params) {
		parameters.putAll(params)
    }

    String getUrl() {
		return url
    }

    void setUrl(String url) {
		this.url = url
    }

    String getHttpMethod() {
		return httpMethod
    }

    void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod
    }

    Map<String, String> getParameters() {
		return parameters
    }

    void setParameters(Map<String, String> parameters) {
		this.parameters = parameters
    }

}
