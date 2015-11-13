/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.dto.ProductCreditPriceDTO;

import tools.dynamia.domain.SimpleEntity;

/**
 *
 * @author mario_2
 */
@Entity
@Table(name = "prd_products_prices")
public class ProductCreditPrice extends SimpleEntity implements SiteAware {

    @OneToOne
    @NotNull
    private Site site;
    @ManyToOne
    @NotNull
    private Product product;
    private int number;
    private String description;
    @NotNull
    private BigDecimal price;

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void setSite(Site site) {
        this.site = site;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void sync(ProductCreditPriceDTO dto) {
        price = dto.getPrice();
        number = dto.getNumber();
        description = dto.getDescription();
    }

}
