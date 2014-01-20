/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author mario
 */
public class ProductSearchForm implements Serializable {

    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Long categoryId;
    private Long brandId;
    private boolean stock;
    private ProductSearchOrder order = ProductSearchOrder.NAME;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }

    public ProductSearchOrder getOrder() {
        return order;
    }

    public void setOrder(ProductSearchOrder order) {
        this.order = order;
    }

}
