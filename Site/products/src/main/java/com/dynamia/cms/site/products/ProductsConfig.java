/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products;

import com.dynamia.cms.site.products.ext.ProductListenerImpl;
import com.dynamia.cms.site.products.api.ProductsListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 *
 * @author mario
 */
@Configuration
public class ProductsConfig {

    public ProductsConfig() {
        System.out.println(">>>>CREANDO " + getClass());
    }

    @Bean(name = "/services/ProductsListenerService")
    public HttpInvokerServiceExporter productListenerExporter() {
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(productListener());
        exporter.setServiceInterface(ProductsListener.class);
        return exporter;
    }

    @Bean
    public ProductsListener productListener() {
        return new ProductListenerImpl();
    }

}
