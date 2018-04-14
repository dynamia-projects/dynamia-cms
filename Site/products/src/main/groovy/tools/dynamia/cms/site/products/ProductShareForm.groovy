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
package tools.dynamia.cms.site.products

import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.domain.contraints.Email

import javax.validation.constraints.NotNull

class ProductShareForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7043302060479353034L
    @Email(message = "Ingrese email valido")
	private String friendEmail
    private String friendName
	private String yourName
	private String message
    private String productURL
    @NotNull
	private Long productId
    private Site site

    ProductShareForm() {
		// TODO Auto-generated constructor stub
	}

    ProductShareForm(Site site) {
		super()
        this.site = site
    }

    String getFriendName() {
		return friendName
    }

    void setFriendName(String friendName) {
		this.friendName = friendName
    }

    Site getSite() {
		return site
    }

    void setSite(Site site) {
		this.site = site
    }

    String getFriendEmail() {
		return friendEmail
    }

    void setFriendEmail(String friendEmail) {
		this.friendEmail = friendEmail
    }

    String getYourName() {
		return yourName
    }

    void setYourName(String yourName) {
		this.yourName = yourName
    }

    String getMessage() {
		return message
    }

    void setMessage(String message) {
		this.message = message
    }

    String getProductURL() {
		return productURL
    }

    void setProductURL(String productURL) {
		this.productURL = productURL
    }

    Long getProductId() {
		return productId
    }

    void setProductId(Long productId) {
		this.productId = productId
    }

}
