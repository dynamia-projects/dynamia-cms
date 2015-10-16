/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.domain;

import com.dynamia.cms.site.core.Orderable;
import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.dto.ProductDetailDTO;
import com.dynamia.tools.domain.SimpleEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author mario
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
	@Column(name="detorder")
	private int order;

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
	}

}
