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
package tools.dynamia.cms.products.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.core.Orderable
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.products.dto.ProductCategoryDetailDTO
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_category_details")
class ProductCategoryDetail extends SimpleEntity implements SiteAware, Orderable, Cloneable {

    @OneToOne
    @NotNull
    private Site site
    @NotEmpty
    private String name
    @Column(name = "detvalues", length = 2000)
    private String values
    private Long externalRef
    @Column(name = "detorder")
    private int order
    private long stock
    private long totalRef

    @Transient
    private List<String> currentValues

    @Transient
    private boolean selected
    @Transient
    private String selectedValue

    @ManyToOne
    @JsonIgnore
    private ProductCategory category

    private boolean filterable = true

    List<String> getCurrentValues() {
        if (currentValues == null) {
            currentValues = new ArrayList<String>()
        }
        return currentValues
    }

    long getStock() {
        return stock
    }

    void setStock(long stock) {
        this.stock = stock
    }

    long getTotalRef() {
        return totalRef
    }

    void setTotalRef(long totalRef) {
        this.totalRef = totalRef
    }

    boolean isFilterable() {
        return filterable
    }

    void setFilterable(boolean filterable) {
        this.filterable = filterable
    }

    int getOrder() {
        return order
    }

    void setOrder(int order) {
        this.order = order
    }

    Site getSite() {
        return site
    }

    void setSite(Site site) {
        this.site = site
    }

    ProductCategory getCategory() {
        return category
    }

    void setCategory(ProductCategory category) {
        this.category = category
    }

    Long getExternalRef() {
        return externalRef
    }

    void setExternalRef(Long externalRef) {
        this.externalRef = externalRef
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getValues() {
        return values
    }

    void setValues(String values) {
        this.values = values
    }

    void sync(ProductCategoryDetailDTO dto) {
        this.name = dto.name
        this.values = dto.values
        this.externalRef = dto.externalRef
        this.order = dto.order
        this.filterable = dto.filterable

    }

    boolean isSelected() {
        return selected
    }

    void setSelected(boolean selected) {
        this.selected = selected
    }

    String getSelectedValue() {
        return selectedValue
    }

    void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue
    }

    @Override
    ProductCategoryDetail clone() {
        ProductCategoryDetail clone = new ProductCategoryDetail()
        clone.currentValues = currentValues
        BeanUtils.setupBean(clone, this)

        return clone
    }
}
