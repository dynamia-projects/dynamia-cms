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
package tools.dynamia.cms.products
/**
 * @author Mario Serrano Leones
 */
class ProductSearchForm implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 9079552167006719550L
    private String name
    private BigDecimal minPrice
    private BigDecimal maxPrice
    private Long categoryId
    private Long brandId
    private boolean stock
    private ProductSearchOrder order = ProductSearchOrder.NAME
    private String detail
    private String detail2
    private String detail3
    private String detail4

    private Map<String, Object> attributes = new HashMap<>()

    String getDetail() {
        return detail
    }

    void setDetail(String detail) {
        this.detail = detail
    }

    static long getSerialversionuid() {
        return serialVersionUID
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    BigDecimal getMinPrice() {
        return minPrice
    }

    void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice
    }

    BigDecimal getMaxPrice() {
        return maxPrice
    }

    void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice
    }

    Long getCategoryId() {
        return categoryId
    }

    void setCategoryId(Long categoryId) {
        this.categoryId = categoryId
    }

    Long getBrandId() {
        return brandId
    }

    void setBrandId(Long brandId) {
        this.brandId = brandId
    }

    boolean isStock() {
        return stock
    }

    void setStock(boolean stock) {
        this.stock = stock
    }

    ProductSearchOrder getOrder() {
        return order
    }

    void setOrder(ProductSearchOrder order) {
        this.order = order
    }

    String getDetail2() {
        return detail2
    }

    void setDetail2(String detail2) {
        this.detail2 = detail2
    }

    String getDetail3() {
        return detail3
    }

    void setDetail3(String detail3) {
        this.detail3 = detail3
    }

    String getDetail4() {
        return detail4
    }

    void setDetail4(String detail4) {
        this.detail4 = detail4
    }

    void setAttribute(String name, Object value) {
        attributes.put(name, value)
    }

    Object getAttribute(String name) {
        return attributes.get(name)
    }
}
