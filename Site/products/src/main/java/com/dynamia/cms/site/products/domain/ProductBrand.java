/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.domain;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.dto.ProductBrandDTO;
import com.dynamia.tools.domain.SimpleEntity;
import com.dynamia.tools.domain.contraints.NotEmpty;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.BatchSize;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "prd_brands")
@BatchSize(size = 40)
public class ProductBrand extends SimpleEntity {

    @OneToOne
    @NotNull
    private Site site;

    @NotEmpty
    private String name;
    private String website;
    private String description;
    private Long externalRef;
    private String image;

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void sync(ProductBrandDTO r) {
        description = r.getDescription();
        externalRef = r.getExternalRef();
        image = r.getImage();
        name = r.getName();
        website = r.getWebsite();
    }

    @Override
    public String toString() {
        return getName();
    }

}
