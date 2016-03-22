/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dynamia.cms.site.products.clients;

import com.dynamia.cms.site.products.api.ProductsDatasource;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 *
 * @author Mario Serrano Leones
 */
public class ProductsDatasourceClient {

    private String serviceURL;
    private String username;
    private String password;
    private HttpInvokerProxyFactoryBean factory;

    private void init() {
        HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
        fb.setServiceInterface(ProductsDatasource.class);
        fb.setServiceUrl(serviceURL);
        fb.afterPropertiesSet();
        this.factory = fb;
    }

    public ProductsDatasource getProxy() {
        if (factory == null) {
            init();
        }

        return (ProductsDatasource) factory.getObject();
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
