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
