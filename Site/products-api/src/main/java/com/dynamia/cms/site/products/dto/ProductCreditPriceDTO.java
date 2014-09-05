/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author mario_2
 */
public class ProductCreditPriceDTO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5111921265189786308L;
	private ProductDTO product;
    private int number;
    private String description;
    private BigDecimal price;

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
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

}
