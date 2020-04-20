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

import tools.dynamia.cms.core.domain.SiteSimpleEntity

import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leons
 */
@Entity
@Table(name = "prd_related_products")
class RelatedProduct extends SiteSimpleEntity {

    private Long externalRef
    @OneToOne
    private ProductCategory targetCategory
    @OneToOne
    private Product targetProduct
    @OneToOne
    @NotNull
    private Product product
    private boolean gift
    private boolean required
    private boolean active = true
    private BigDecimal price

    Long getExternalRef() {
        return externalRef
    }

    void setExternalRef(Long externalRef) {
        this.externalRef = externalRef
    }

    ProductCategory getTargetCategory() {
        return targetCategory
    }

    void setTargetCategory(ProductCategory targetCategory) {
        this.targetCategory = targetCategory
    }

    Product getTargetProduct() {
        return targetProduct
    }

    void setTargetProduct(Product targetProduct) {
        this.targetProduct = targetProduct
    }

    Product getProduct() {
        return product
    }

    void setProduct(Product product) {
        this.product = product
    }

    boolean isGift() {
        return gift
    }

    void setGift(boolean gift) {
        this.gift = gift
    }

    boolean isRequired() {
        return required
    }

    void setRequired(boolean required) {
        this.required = required
    }

    boolean isActive() {
        return active
    }

    void setActive(boolean active) {
        this.active = active
    }

    BigDecimal getPrice() {
        return price
    }

    void setPrice(BigDecimal price) {
        this.price = price
    }

    void sync(tools.dynamia.cms.products.dto.RelatedProductDTO dto) {
        this.externalRef = dto.externalRef
        this.gift = dto.gift
        this.active = dto.active
        this.price = dto.price
        this.required = dto.required
    }

}
