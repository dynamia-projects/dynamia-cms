/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.Orderable;
import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.dto.ProductCategoryDTO;
import com.dynamia.tools.domain.SimpleEntity;
import com.dynamia.tools.domain.contraints.NotEmpty;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "prd_categories")
public class ProductCategory extends SimpleEntity implements SiteAware, Orderable {

	@OneToOne
	@NotNull
	private Site site;

	@ManyToOne
	private ProductCategory parent;
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
	private List<ProductCategory> subcategories = new ArrayList<>();
	@NotNull
	@NotEmpty(message = "Enter product category name")
	private String name;
	private String alternateName;
	@Column(name = "catAlias")
	private String alias;
	private String description;
	private boolean active;
	private Long externalRef;
	@OneToOne
	private ProductCategory relatedCategory;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<ProductCategoryDetail> details = new ArrayList<>();

	@Column(name = "catOrder")
	private int order;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getAlternateName() {
		return alternateName;
	}

	public void setAlternateName(String alternateName) {
		this.alternateName = alternateName;
	}

	public ProductCategory getRelatedCategory() {
		return relatedCategory;
	}

	public void setRelatedCategory(ProductCategory relatedCategory) {
		this.relatedCategory = relatedCategory;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public List<ProductCategoryDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ProductCategoryDetail> details) {
		this.details = details;
	}

	public Long getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(Long externalRef) {
		this.externalRef = externalRef;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ProductCategory getParent() {
		return parent;
	}

	public void setParent(ProductCategory parent) {
		this.parent = parent;
	}

	public List<ProductCategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<ProductCategory> subcategories) {
		this.subcategories = subcategories;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void sync(ProductCategoryDTO dto) {
		name = dto.getName();
		order = dto.getOrder();
		alternateName = dto.getAlternateName();
		active = dto.isActive();
		description = dto.getDescription();
		externalRef = dto.getExternalRef();

	}

	@Override
	public String toString() {
		return getName();
	}

}
