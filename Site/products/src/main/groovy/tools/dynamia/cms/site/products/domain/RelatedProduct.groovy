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
public class RelatedProduct extends SiteSimpleEntity {

    private Long externalRef;
    @OneToOne
    private ProductCategory targetCategory;
    @OneToOne
    private Product targetProduct;
    @OneToOne
    @NotNull
    private Product product;
    private boolean gift;
    private boolean required;
    private boolean active = true;
    private BigDecimal price;

    public Long getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(Long externalRef) {
        this.externalRef = externalRef;
    }

    public ProductCategory getTargetCategory() {
        return targetCategory;
    }

    public void setTargetCategory(ProductCategory targetCategory) {
        this.targetCategory = targetCategory;
    }

    public Product getTargetProduct() {
        return targetProduct;
    }

    public void setTargetProduct(Product targetProduct) {
        this.targetProduct = targetProduct;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isGift() {
        return gift;
    }

    public void setGift(boolean gift) {
        this.gift = gift;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void sync(RelatedProductDTO dto) {
        this.externalRef = dto.getExternalRef();
        this.gift = dto.isGift();
        this.active = dto.isActive();
        this.price = dto.getPrice();
        this.required = dto.isRequired();
    }

}
