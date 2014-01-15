/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.domain;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.dto.ProductDTO;
import com.dynamia.tools.domain.SimpleEntity;
import com.dynamia.tools.domain.contraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "prd_products")
public class Product extends SimpleEntity {

    @OneToOne
    @NotNull
    private Site site;

    @NotNull
    @NotEmpty(message = "Enter product name")
    private String name;
    @Column(length = 1000)
    private String description;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String longDescription;
    @NotNull
    private String sku;
    private Long externalRef;

    private BigDecimal price;
    private BigDecimal lastPrice;
    private Long stock;
    private boolean active;
    private boolean featured;
    private boolean sale;
    private String image;

    @Column(length = 5000)
    private String tags;

    @OneToOne
    @NotNull(message = "Select product category")
    private ProductCategory category;

    @OneToOne
    private ProductBrand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductDetail> details = new ArrayList<>();

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
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

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(Long externalRef) {
        this.externalRef = externalRef;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public ProductBrand getBrand() {
        return brand;
    }

    public void setBrand(ProductBrand brand) {
        this.brand = brand;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<ProductDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ProductDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return name;
    }

    public void sync(ProductDTO dto) {
        active = dto.isActive();
        description = dto.getDescription();
        externalRef = dto.getExternalRef();
        featured = dto.isFeatured();
        image = dto.getImage();
        lastPrice = dto.getLastPrice();
        longDescription = dto.getLongDescription();
        name = dto.getName();
        price = dto.getPrice();
        sale = dto.isSale();
        sku = dto.getSku();
        stock = dto.getStock();
        tags = dto.getTags();
    }

}
