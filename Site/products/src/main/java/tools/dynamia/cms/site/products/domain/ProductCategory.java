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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import tools.dynamia.cms.site.core.Orderable;
import tools.dynamia.cms.site.core.View;
import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.products.dto.ProductCategoryDTO;

import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.contraints.NotEmpty;

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_categories")
@BatchSize(size = 50)
public class ProductCategory extends SimpleEntity implements SiteAware, Orderable {

	@OneToOne
	@NotNull
	private Site site;

	@ManyToOne
	@JsonIgnore
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
	@JsonIgnore
	private ProductCategory relatedCategory;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	@OrderBy("order")
	@JsonIgnore
	private List<ProductCategoryDetail> details = new ArrayList<>();

	@Column(name = "catOrder")	
	private int order;

	@OneToOne
	@JsonIgnore
	private ProductTemplate template;

	@OneToOne
	@JsonIgnore
	private ProductTemplate alternateTemplate;

	public ProductTemplate getAlternateTemplate() {
		return alternateTemplate;
	}

	public void setAlternateTemplate(ProductTemplate alternateTemplate) {
		this.alternateTemplate = alternateTemplate;
	}

	public ProductTemplate getTemplate() {
		return template;
	}

	public void setTemplate(ProductTemplate template) {
		this.template = template;
	}

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
