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
package com.dynamia.cms.site.products.dto;

import java.io.Serializable;

/**
 *
 * @author Mario Serrano Leones
 */
public class ProductDetailDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1265515027309763424L;
    private ProductDTO product;
    private String name;

    private String value;
    private String description;
    private Long externalRef;
    private int order;
    private String imageURL;
    private boolean featured;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public Long getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(Long externalRef) {
        this.externalRef = externalRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

}
