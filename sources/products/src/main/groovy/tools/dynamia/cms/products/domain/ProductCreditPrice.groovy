/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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

import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.SimpleEntity

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_products_prices")
class ProductCreditPrice extends SimpleEntity implements SiteAware {

	@OneToOne
	@NotNull
	private Site site
    @ManyToOne
	@NotNull
	private Product product
    private int number
    private String description
    @NotNull
	private BigDecimal price

    @Override
    Site getSite() {
		return site
    }

	@Override
    void setSite(Site site) {
		this.site = site
    }

    Product getProduct() {
		return product
    }

    void setProduct(Product product) {
		this.product = product
    }

    int getNumber() {
		return number
    }

    void setNumber(int number) {
		this.number = number
    }

    String getDescription() {
		return description
    }

    void setDescription(String description) {
		this.description = description
    }

    BigDecimal getPrice() {
		return price
    }

    void setPrice(BigDecimal price) {
		this.price = price
    }

    void sync(tools.dynamia.cms.products.dto.ProductCreditPriceDTO dto) {
		price = dto.price
        number = dto.number
        description = dto.description
    }

}
