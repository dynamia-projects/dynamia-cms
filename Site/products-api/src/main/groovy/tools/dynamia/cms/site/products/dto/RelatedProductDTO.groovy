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
package tools.dynamia.cms.site.products.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Mario Serrano Leons
 */
public class RelatedProductDTO implements Serializable {

    private Long externalRef;
    private Long targetCategoryExternalRef;
    private Long targetProductExternalRef;
    private Long productExternalRef;

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

    public Long getTargetCategoryExternalRef() {
        return targetCategoryExternalRef;
    }

    public void setTargetCategoryExternalRef(Long targetCategoryExternalRef) {
        this.targetCategoryExternalRef = targetCategoryExternalRef;
    }

    public Long getTargetProductExternalRef() {
        return targetProductExternalRef;
    }

    public void setTargetProductExternalRef(Long targetProductExternalRef) {
        this.targetProductExternalRef = targetProductExternalRef;
    }

    public Long getProductExternalRef() {
        return productExternalRef;
    }

    public void setProductExternalRef(Long productExternalRef) {
        this.productExternalRef = productExternalRef;
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

}
