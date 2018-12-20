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
package tools.dynamia.cms.shoppingcart.domain

import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.SimpleEntity

import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "sc_shipping_companies")
class ShippingCompany extends SimpleEntity implements SiteAware {

	@OneToOne
	private Site site
    private String name
    private String website
    private String trackingSiteURL
    private Long externalRef

    Site getSite() {
		return site
    }

    void setSite(Site site) {
		this.site = site
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

    String getTrackingSiteURL() {
		return trackingSiteURL
    }

    void setTrackingSiteURL(String trackingSiteURL) {
		this.trackingSiteURL = trackingSiteURL
    }

    Long getExternalRef() {
		return externalRef
    }

    void setExternalRef(Long externalRef) {
		this.externalRef = externalRef
    }

	@Override
    String toString() {
		return name
    }

    void addItem(ShoppingCartItem item, int qty) {
	}
}
