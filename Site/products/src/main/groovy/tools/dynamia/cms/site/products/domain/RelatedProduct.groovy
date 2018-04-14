/*
 * Copyright 2016 mario.
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
package tools.dynamia.cms.site.products.domain

import tools.dynamia.cms.site.core.domain.SiteSimpleEntity
import tools.dynamia.cms.site.products.dto.RelatedProductDTO

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

    void sync(RelatedProductDTO dto) {
        this.externalRef = dto.externalRef
        this.gift = dto.gift
        this.active = dto.active
        this.price = dto.price
        this.required = dto.required
    }

}
