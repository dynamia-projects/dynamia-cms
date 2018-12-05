/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tools.dynamia.cms.products.dto
/**
 *
 * @author Mario Serrano Leons
 */
class RelatedProductDTO implements Serializable {

    private Long externalRef
    private Long targetCategoryExternalRef
    private Long targetProductExternalRef
    private Long productExternalRef

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

    Long getTargetCategoryExternalRef() {
        return targetCategoryExternalRef
    }

    void setTargetCategoryExternalRef(Long targetCategoryExternalRef) {
        this.targetCategoryExternalRef = targetCategoryExternalRef
    }

    Long getTargetProductExternalRef() {
        return targetProductExternalRef
    }

    void setTargetProductExternalRef(Long targetProductExternalRef) {
        this.targetProductExternalRef = targetProductExternalRef
    }

    Long getProductExternalRef() {
        return productExternalRef
    }

    void setProductExternalRef(Long productExternalRef) {
        this.productExternalRef = productExternalRef
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

}
