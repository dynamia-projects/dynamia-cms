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

class LocationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L
    private String name
    private String code
    private String externalRef

    LocationDTO() {
		// TODO Auto-generated constructor stub
	}

    LocationDTO(String name, String code) {
		super()
        this.name = name
        this.code = code
    }

    String getExternalRef() {
		return externalRef
    }

    void setExternalRef(String externalRef) {
		this.externalRef = externalRef
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getCode() {
		return code
    }

    void setCode(String code) {
		this.code = code
    }

}
