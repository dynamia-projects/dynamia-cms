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
package tools.dynamia.cms.site.products.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tools.dynamia.cms.site.core.Orderable;
import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.products.dto.ProductCategoryDetailDTO;
import tools.dynamia.commons.BeanUtils;
import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.contraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_category_details")
public class ProductCategoryDetail extends SimpleEntity implements SiteAware, Orderable, Cloneable {

    @OneToOne
    @NotNull
    private Site site;
    @NotEmpty
    private String name;
    @Column(name = "detvalues")
    private String values;
    private Long externalRef;
    @Column(name = "detorder")
    private int order;
    private long stock;
    private long totalRef;

    @Transient
    private List<String> currentValues;

    @Transient
    private boolean selected;
    @Transient
    private String selectedValue;

    @ManyToOne
    @JsonIgnore
    private ProductCategory category;

    private boolean filterable = true;

    public List<String> getCurrentValues() {
        if (currentValues == null) {
            currentValues = new ArrayList<String>();
        }
        return currentValues;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public long getTotalRef() {
        return totalRef;
    }

    public void setTotalRef(long totalRef) {
        this.totalRef = totalRef;
    }

    public boolean isFilterable() {
        return filterable;
    }

    public void setFilterable(boolean filterable) {
        this.filterable = filterable;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Long getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(Long externalRef) {
        this.externalRef = externalRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public void sync(ProductCategoryDetailDTO dto) {
        this.name = dto.getName();
        this.values = dto.getValues();
        this.externalRef = dto.getExternalRef();
        this.order = dto.getOrder();
        this.filterable = dto.isFilterable();

    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    @Override
    public ProductCategoryDetail clone() {
        ProductCategoryDetail clone = new ProductCategoryDetail();
        clone.currentValues = currentValues;
        BeanUtils.setupBean(clone, this);

        return clone;
    }
}
