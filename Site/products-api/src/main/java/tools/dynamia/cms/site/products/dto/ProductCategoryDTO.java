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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mario Serrano Leones
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
