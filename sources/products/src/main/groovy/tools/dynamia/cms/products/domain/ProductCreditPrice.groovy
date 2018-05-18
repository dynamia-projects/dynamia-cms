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
