/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mario
 */
public class ProductCategoryDTO implements Serializable {

    private ProductCategoryDTO parent;

    private List<ProductCategoryDTO> subcategories = new ArrayList<>();

    private String name;
    private String description;
    private boolean active;
    private Long externalRef;

    private List<ProductCategoryDetailDTO> details = new ArrayList<>();

    public List<ProductCategoryDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<ProductCategoryDetailDTO> details) {
        this.details = details;
    }

    public Long getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(Long externalRef) {
        this.externalRef = externalRef;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ProductCategoryDTO getParent() {
        return parent;
    }

    public void setParent(ProductCategoryDTO parent) {
        this.parent = parent;
    }

    public List<ProductCategoryDTO> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<ProductCategoryDTO> subcategories) {
        this.subcategories = subcategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
