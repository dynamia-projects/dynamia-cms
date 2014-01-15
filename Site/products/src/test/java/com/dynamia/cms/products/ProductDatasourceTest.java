/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.products;

import com.dynamia.cms.site.products.api.ProductsDatasource;
import com.dynamia.cms.site.products.clients.ProductsDatasourceClient;
import com.dynamia.cms.site.products.dto.ProductDTO;
import com.dynamia.cms.site.products.dto.ProductCategoryDTO;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author mario
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
        ds.getProduct(1L);
        assertNotNull(ds);
    }

    @Test
    public void testCategoryList() {
        ProductsDatasource ds = client.getProxy();
        List<ProductCategoryDTO> categories = ds.getCategories();
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
        List<ProductDTO> products = ds.getProducts();
        assertFalse(products.isEmpty());

        for (ProductDTO p : products) {
            System.out.println(p.getSku()+" "+p.getExternalRef()+"  "+ p.getName());            
        }
        System.out.println("\n\n"+products.size()+" products");
    }

}
