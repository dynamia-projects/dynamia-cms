/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.clients;

import com.dynamia.cms.site.products.api.ProductsListener;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 *
 * @author mario
 */
public class ProductsListenerClient {

    private String serviceURL;
    private String username;
    private String password;
    private HttpInvokerProxyFactoryBean factory;

    private void init() {
        HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
        fb.setServiceInterface(ProductsListener.class);
        fb.setServiceUrl(serviceURL);        
        fb.afterPropertiesSet();        
        this.factory = fb;
    }

    public ProductsListener getProxy() {
        if (factory == null) {
            init();
        }

        return (ProductsListener) factory.getObject();
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
