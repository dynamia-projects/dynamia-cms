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
package tools.dynamia.cms.products.dto
/**
 *
 * @author Mario Serrano Leones
 */
class ProductBrandDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7706501653302805197L
    private String name
    private String website
    private String description
    private Long externalRef
    private String image

    String getImage() {
        return image
    }

    void setImage(String image) {
        this.image = image
    }

    Long getExternalRef() {
        return externalRef
    }

    void setExternalRef(Long externalRef) {
        this.externalRef = externalRef
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getWebsite() {
        return website
    }

    void setWebsite(String website) {
        this.website = website
    }

    String getDescription() {
        return description
    }

    void setDescription(String description) {
        this.description = description
    }

}
