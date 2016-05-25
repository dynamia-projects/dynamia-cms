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
package tools.dynamia.cms.site.products;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.scheduling.annotation.EnableAsync;

import tools.dynamia.cms.site.products.api.ProductsListener;
import tools.dynamia.cms.site.products.listeners.ProductListenerImpl;

/**
 *
 * @author Mario Serrano Leones
 */
@Configuration
@EnableAsync
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
