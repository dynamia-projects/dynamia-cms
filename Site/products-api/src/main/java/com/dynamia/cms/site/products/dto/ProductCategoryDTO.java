/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mario
 */
public class ProductCategoryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6391246496268751494L;

	private ProductCategoryDTO parent;

	private List<ProductCategoryDTO> subcategories = new ArrayList<>();

	private String name;
	private String alternateName;
	private String description;
	private boolean active;
	private Long externalRef;
	private Long relatedCategoryExternalRef;
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

	private List<ProductCategoryDetailDTO> details = new ArrayList<>();

	public Long getRelatedCategoryExternalRef() {
		return relatedCategoryExternalRef;
	}

	public void setRelatedCategoryExternalRef(Long relatedCategoryExternalRef) {
		this.relatedCategoryExternalRef = relatedCategoryExternalRef;
	}

	public List<ProductCategoryDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<ProductCategoryDetailDTO> details) {
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

	public ProductCategoryDTO getParent() {
		return parent;
	}

	public void setParent(ProductCategoryDTO parent) {
		this.parent = parent;
	}

	public List<ProductCategoryDTO> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<ProductCategoryDTO> subcategories) {
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

}
