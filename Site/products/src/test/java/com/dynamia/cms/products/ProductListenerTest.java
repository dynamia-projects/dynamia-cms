/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.products;

import com.dynamia.cms.site.products.api.ProductsListener;
import com.dynamia.cms.site.products.clients.ProductsListenerClient;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author mario
 */
public class ProductListenerTest {

    private ProductsListenerClient client;
    private String serviceURL = "http://localhost:8080/cms/ProductsListenerService";

    @Before
    public void init() {
        client = new ProductsListenerClient();
        client.setServiceURL(serviceURL);
    }

    @Test
    public void testConnection() {
        ProductsListener listener = client.getProxy();
        listener.productChanged(1L);
        assertNotNull(listener);
    }

}
