/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package tools.dynamia.cms.core.dto

class SiteDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L
    private String name
    private String key
    private String domain

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getKey() {
		return key
    }

    void setKey(String key) {
		this.key = key
    }

    String getDomain() {
		return domain
    }

    void setDomain(String domain) {
		this.domain = domain
    }

}
