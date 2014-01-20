/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products;

/**
 *
 * @author mario
 */
public enum ProductSearchOrder {

    NAME("name", true), MINPRICE("price", true), MAXPRICE("price", false), RATE("rate", true);

    private String field;
    private boolean asc;

    ProductSearchOrder(String field, boolean asc) {
        this.field = field;
        this.asc = asc;
    }

    public String getField() {
        return field;
    }

    public boolean isAsc() {
        return asc;
    }

}
