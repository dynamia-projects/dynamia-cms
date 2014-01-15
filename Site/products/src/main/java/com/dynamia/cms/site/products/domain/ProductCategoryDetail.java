/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.domain;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.domain.SimpleEntity;
import com.dynamia.tools.domain.contraints.NotEmpty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "prd_category_details")
public class ProductCategoryDetail extends SimpleEntity {

    @OneToOne
    @NotNull
    private Site site;
    @NotEmpty
    private String name;
    @Column(name = "detvalues")
    private String values;
    private Long externalRef;

    @ManyToOne
    private ProductCategory category;

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

}
