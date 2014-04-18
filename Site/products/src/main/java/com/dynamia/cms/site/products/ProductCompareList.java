/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products;

import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.tools.commons.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author mario_2
 */
@Component
@Scope("session")
public class ProductCompareList implements Serializable {

    private List<Product> products = new ArrayList<>();

    public void add(Product product) {
        if (product != null && !products.contains(product)) {
            products.add(product);
        }
    }

    public void remove(Product product) {
        products.remove(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        List<Long> ids = new ArrayList<>();
        for (Product product : products) {
            ids.add(product.getId());
        }
        return StringUtils.arrayToCommaDelimitedString(ids.toArray());
    }

    public void clear() {
        products = new ArrayList<>();
    }
}
