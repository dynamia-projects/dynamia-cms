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
package com.dynamia.cms.site.products;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Mario Serrano Leones
 */
public class ProductSearchForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9079552167006719550L;
	private String name;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private Long categoryId;
	private Long brandId;
	private boolean stock;
	private ProductSearchOrder order = ProductSearchOrder.NAME;
	private String detail;

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public boolean isStock() {
		return stock;
	}

	public void setStock(boolean stock) {
		this.stock = stock;
	}

	public ProductSearchOrder getOrder() {
		return order;
	}

	public void setOrder(ProductSearchOrder order) {
		this.order = order;
	}

}
