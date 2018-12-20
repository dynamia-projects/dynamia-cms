/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.products

import tools.dynamia.cms.core.domain.Site
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
