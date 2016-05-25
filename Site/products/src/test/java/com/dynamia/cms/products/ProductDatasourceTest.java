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
package com.dynamia.cms.products;

import tools.dynamia.cms.site.products.api.ProductsDatasource;
import tools.dynamia.cms.site.products.clients.ProductsDatasourceClient;
import tools.dynamia.cms.site.products.dto.ProductDTO;
import tools.dynamia.cms.site.products.dto.ProductCategoryDTO;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Mario Serrano Leones
 */
public class ProductDatasourceTest {

    private ProductsDatasourceClient client;
    private String serviceURL = "http://localhost:8181/services/ProductsDatasourceService";

    @Before
    public void init() {
        client = new ProductsDatasourceClient();
        client.setServiceURL(serviceURL);
    }

    @Test
    public void testConnection() {
        ProductsDatasource ds = client.getProxy();
        ds.getProduct(1L,null);
        assertNotNull(ds);
    }

    @Test
    public void testCategoryList() {
        ProductsDatasource ds = client.getProxy();
        List<ProductCategoryDTO> categories = ds.getCategories(null);
        assertFalse(categories.isEmpty());

        for (ProductCategoryDTO cat : categories) {
            System.out.println(cat.getName());
            for (ProductCategoryDTO subcat : cat.getSubcategories()) {
                System.out.println("   " + subcat.getName());
            }
        }
        
        System.out.println("\n\n"+categories.size()+" main categories\n\n");
    }

    @Test
    public void testProductList() {
        ProductsDatasource ds = client.getProxy();
        List<ProductDTO> products = ds.getProducts(null);
        assertFalse(products.isEmpty());

        for (ProductDTO p : products) {
            System.out.println(p.getSku()+" "+p.getExternalRef()+"  "+ p.getName());            
        }
        System.out.println("\n\n"+products.size()+" products");
    }

}
