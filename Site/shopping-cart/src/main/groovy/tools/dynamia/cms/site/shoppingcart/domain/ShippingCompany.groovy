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
package tools.dynamia.cms.site.shoppingcart.domain

import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Site
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
		return getName()
    }

    void addItem(ShoppingCartItem item, int qty) {
	}
}
