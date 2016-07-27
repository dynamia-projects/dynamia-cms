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
package tools.dynamia.cms.site.products.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import tools.dynamia.cms.site.core.Orderable;
import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.products.dto.ProductDetailDTO;

import tools.dynamia.domain.SimpleEntity;

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_details")
public class ProductDetail extends SimpleEntity implements SiteAware, Orderable {

	@OneToOne
	@NotNull
	private Site site;

	@ManyToOne
	private Product product;
	private String name;
	@Column(name = "detvalue")
	private String value;
	private String description;
	private Long externalRef;
	@Column(name = "detorder")
	private int order;
	private String imageURL;
	private boolean featured;
	private boolean filterale;
	private String value2;
	private String color;
	private String url;
	private String url2;

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl2() {
		return url2;
	}

	public void setUrl2(String url2) {
		this.url2 = url2;
	}

	public boolean isFilterale() {
		return filterale;
	}

	public void setFilterale(boolean filterale) {
		this.filterale = filterale;
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
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

	public void sync(ProductDetailDTO dto) {
		description = dto.getDescription();
		externalRef = dto.getExternalRef();
		name = dto.getName();
		value = dto.getValue();
		order = dto.getOrder();
		imageURL = dto.getImageURL();
		featured = dto.isFeatured();
		filterale = dto.isFilterable();
		value2 = dto.getValue2();
		url = dto.getUrl();
		url2 = dto.getUrl2();
		color = dto.getColor();
	}

}
