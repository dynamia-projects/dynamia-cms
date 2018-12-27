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
package tools.dynamia.cms.products.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.mail.domain.MailAccount
import tools.dynamia.cms.mail.domain.MailTemplate
import tools.dynamia.domain.SimpleEntity

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_sites_config")
class ProductsSiteConfig extends SimpleEntity implements SiteAware {

	@OneToOne
	@NotNull
	@JoinColumn(unique = true)
	Site site

    String pricePattern = ""
    String defaultCurrency = ""

    @JsonIgnore
	String datasourceURL
    @JsonIgnore
	String datasourceUsername
    @JsonIgnore
	String datasourcePassword
    @JsonIgnore
	String datasourceImagesURL
    @JsonIgnore
	String datasourceBrandImagesURL
    @JsonIgnore
	String datasourceStoreImagesURL
    @JsonIgnore
	String datasourceStoreContactImagesURL

    @JsonIgnore
	String token

    @Temporal(TemporalType.TIMESTAMP)
	Date lastSync

    int productsPerPage

    int productsStockToShow

    boolean shopEnabled

    boolean quoteEnabled

    boolean showBadges

    boolean synchronizationEnabled

    @OneToOne
	@JsonIgnore
	MailAccount mailAccount
    @OneToOne
	@JsonIgnore
	MailTemplate shareProductMailTemplate
    @OneToOne
	@JsonIgnore
	MailTemplate orderCompletedMailTemplate

    @OneToMany(mappedBy = "siteConfig", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	List<ProductsSiteConfigParameter> parameters = new ArrayList<>()
    @JsonIgnore
	int minStock
    @JsonIgnore
	boolean syncStockDetails
    @JsonIgnore
	boolean syncProductDetails
    @JsonIgnore
	boolean syncProductImages
    @JsonIgnore
	boolean syncProductCreditPrices
    @JsonIgnore
	boolean syncStoreContacts

    @Column(length = 4000)
	@JsonIgnore
	String priceUserGroup
    @JsonIgnore
	@Column(length = 4000)
	String price2UserGroup
    @JsonIgnore
	@Column(length = 4000)
	String costUserGroup

    @JsonIgnore
	String reviewsConnectorURL
    String brandLabel = "Marca"


    String getBrandLabel(){
        if(!brandLabel){
            brandLabel = "Marca"
        }
        return brandLabel
    }

    int getProductsPerPage() {
		if (productsPerPage <= 0) {
			productsPerPage = 16
        }
		return productsPerPage
    }



    int getProductsStockToShow() {
		if (productsStockToShow <= 0) {
			productsStockToShow = 5
        }
		return productsStockToShow
    }



	@JsonIgnore
    Map<String, String> getParametersAsMap() {
		Map<String, String> params = new HashMap<>()
        for (ProductsSiteConfigParameter param : (parameters)) {
			params.put(param.name, param.value)
        }
		return params
    }

}
