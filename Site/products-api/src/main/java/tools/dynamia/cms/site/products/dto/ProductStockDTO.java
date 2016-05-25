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
package tools.dynamia.cms.site.products.dto;

import java.io.Serializable;

/**
 *
 * @author Mario Serrano Leones
 */
public class ProductStockDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4070851961458154984L;
    private Long productExternalRef;
    private Long storeExternalRef;
    private long stock;

    public Long getProductExternalRef() {
        return productExternalRef;
    }

    public void setProductExternalRef(Long productExternalRef) {
        this.productExternalRef = productExternalRef;
    }

    public Long getStoreExternalRef() {
        return storeExternalRef;
    }

    public void setStoreExternalRef(Long storeExternalRef) {
        this.storeExternalRef = storeExternalRef;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

}
