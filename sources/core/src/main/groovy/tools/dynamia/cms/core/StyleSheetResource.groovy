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
package tools.dynamia.cms.core

class StyleSheetResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2763610197937288114L
    private String name
    private String href

    StyleSheetResource() {

	}

    StyleSheetResource(String name, String href) {
		super()
        this.name = name
        this.href = href
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getHref() {
		return href
    }

    void setHref(String href) {
		this.href = href
    }

	@Override
    int hashCode() {
		final int prime = 31
        int result = 1
        result = prime * result + ((name == null) ? 0 : name.hashCode())
        return result
    }

	@Override
    boolean equals(Object obj) {
		if (this == obj)
			return true
        if (obj == null)
			return false
        if (getClass() != obj.class)
			return false
        StyleSheetResource other = (StyleSheetResource) obj
        if (name == null) {
			if (other.name != null)
				return false
        } else if (!name.equals(other.name))
			return false
        return true
    }

}
