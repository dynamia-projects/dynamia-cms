/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.dto;

import java.io.Serializable;

/**
 *
 * @author mario_2
 */
public class ProductStockDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4070851961458154984L;
	private Long productExternalRef;
    private Long storeExternalRef;
    private long stock;

    public Long getProductExternalRef() {
        return productExternalRef;
    }

    public void setProductExternalRef(Long productExternalRef) {
        this.productExternalRef = productExternalRef;
    }

    public Long getStoreExternalRef() {
        return storeExternalRef;
    }

    public void setStoreExternalRef(Long storeExternalRef) {
        this.storeExternalRef = storeExternalRef;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

}
