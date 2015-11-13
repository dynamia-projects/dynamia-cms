/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.Orderable;
import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.dto.ProductCategoryDetailDTO;

import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.contraints.NotEmpty;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "prd_category_details")
public class ProductCategoryDetail extends SimpleEntity implements SiteAware, Orderable {

	@OneToOne
	@NotNull
	private Site site;
	@NotEmpty
	private String name;
	@Column(name = "detvalues")
	private String values;
	private Long externalRef;
	@Column(name = "detorder")
	private int order;

	@Transient
	private List<String> currentValues;

	@ManyToOne
	private ProductCategory category;

	private boolean filterable = true;

	public List<String> getCurrentValues() {
		if (currentValues == null) {
			currentValues = new ArrayList<String>();
		}
		return currentValues;
	}

	public boolean isFilterable() {
		return filterable;
	}

	public void setFilterable(boolean filterable) {
		this.filterable = filterable;
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

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
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

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public void sync(ProductCategoryDetailDTO dto) {
		this.name = dto.getName();
		this.values = dto.getValues();
		this.externalRef = dto.getExternalRef();
		this.order = dto.getOrder();
		this.filterable = dto.isFilterable();

	}

}
