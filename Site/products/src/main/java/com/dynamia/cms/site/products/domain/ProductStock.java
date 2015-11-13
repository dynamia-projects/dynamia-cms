/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.dto.ProductStockDTO;

import tools.dynamia.domain.SimpleEntity;

/**
 *
 * @author mario_2
 */
@Entity
@Table(name = "prd_stocks")
public class ProductStock extends SimpleEntity implements SiteAware {

    @OneToOne
    @NotNull
    private Site site;

    @NotNull
    @ManyToOne
    private Product product;
    @OneToOne
    @NotNull
    private Store store;
    @Min(value = 0)
    private long stock;

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public void sync(ProductStockDTO dto) {
        stock = dto.getStock();
    }

}
