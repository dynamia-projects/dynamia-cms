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

import com.dynamia.cms.site.products.api.DataChangedEvent;
import com.dynamia.cms.site.products.api.ProductsListener;
import com.dynamia.cms.site.products.clients.ProductsListenerClient;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Mario Serrano Leones
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
        assertNotNull(listener);
        
        listener.productChanged(new DataChangedEvent("123", 1L));
        
    }

}
