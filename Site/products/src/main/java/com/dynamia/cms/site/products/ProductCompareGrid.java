/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductDetail;
import com.dynamia.cms.site.products.services.ProductsService;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.commons.collect.ArrayListMultiMap;
import tools.dynamia.commons.collect.MultiMap;

/**
 *
 * @author mario_2
 */
public class ProductCompareGrid implements Serializable {

    private ProductsService service;
    private Set<String> features = new TreeSet<>();
    private MultiMap<String, ProductDetail> data = new ArrayListMultiMap<String, ProductDetail>();
    private List<Product> products;

    public ProductCompareGrid(ProductsService service, List<Product> products) {
        this.service = service;
        this.products = products;
    }

    public void loadData() {
        List<ProductDetail> allDetails = service.getProductsDetails(products);
        for (ProductDetail productDetail : allDetails) {
            features.add(cleanFeature(productDetail.getName()));
        }

        for (Product product : products) {
            List<ProductDetail> productDetails = getDetails(product, allDetails);
            for (String name : features) {
                ProductDetail det = getDetail(name, productDetails);
                data.put(name, det);
            }
        }

    }

    public MultiMap<String, ProductDetail> getData() {
        return data;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Set<String> getFeatures() {
        return features;
    }

    private List<ProductDetail> getDetails(Product product, List<ProductDetail> allDetails) {
        List<ProductDetail> details = new ArrayList<>();
        for (ProductDetail det : allDetails) {
            if (det.getProduct().equals(product)) {
                details.add(det);
            }
        }
        return details;
    }

    private ProductDetail getDetail(String name, List<ProductDetail> productDetails) {
        for (ProductDetail productDetail : productDetails) {
            if (cleanFeature(productDetail.getName()).equals(name)) {
                return productDetail;
            }
        }
        return null;
    }

    private String cleanFeature(String name) {
        return StringUtils.capitalize(name.trim().toLowerCase());
    }
}
