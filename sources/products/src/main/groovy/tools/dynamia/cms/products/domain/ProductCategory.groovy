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
package tools.dynamia.cms.products.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.BatchSize
import tools.dynamia.cms.core.Orderable
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_categories")
@BatchSize(size = 50)
class ProductCategory extends SimpleEntity implements SiteAware, Orderable {

	@OneToOne
	@NotNull
	private Site site

    @ManyToOne
	@JsonIgnore
	private ProductCategory parent
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
	private List<ProductCategory> subcategories = new ArrayList<>()
    @NotNull
	@NotEmpty(message = "Enter product category name")

	private String name
    private String alternateName
    @Column(name = "catAlias")

	private String alias

    private String description

    private boolean active = true

    private Long externalRef
    @OneToOne
	@JsonIgnore
	private ProductCategory relatedCategory

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	@OrderBy("order")
	@JsonIgnore
	private List<ProductCategoryDetail> details = new ArrayList<>()

    @Column(name = "catOrder")
	private int order

    @OneToOne
	@JsonIgnore
	private ProductTemplate template

    @OneToOne
	@JsonIgnore
	private ProductTemplate alternateTemplate

    private Long productsCount

    Long getProductsCount() {
		return productsCount
    }

    void setProductsCount(Long productsCount) {
		this.productsCount = productsCount
    }

    ProductTemplate getAlternateTemplate() {
		return alternateTemplate
    }

    void setAlternateTemplate(ProductTemplate alternateTemplate) {
		this.alternateTemplate = alternateTemplate
    }

    ProductTemplate getTemplate() {
		return template
    }

    void setTemplate(ProductTemplate template) {
		this.template = template
    }

    int getOrder() {
		return order
    }

    void setOrder(int order) {
		this.order = order
    }

    String getAlternateName() {
		return alternateName
    }

    void setAlternateName(String alternateName) {
		this.alternateName = alternateName
    }

    ProductCategory getRelatedCategory() {
		return relatedCategory
    }

    void setRelatedCategory(ProductCategory relatedCategory) {
		this.relatedCategory = relatedCategory
    }

    String getAlias() {
		return alias
    }

    void setAlias(String alias) {
		this.alias = alias
    }

    Site getSite() {
		return site
    }

    void setSite(Site site) {
		this.site = site
    }

    List<ProductCategoryDetail> getDetails() {
		return details
    }

    void setDetails(List<ProductCategoryDetail> details) {
		this.details = details
    }

    Long getExternalRef() {
		return externalRef
    }

    void setExternalRef(Long externalRef) {
		this.externalRef = externalRef
    }

    boolean isActive() {
		return active
    }

    void setActive(boolean active) {
		this.active = active
    }

    ProductCategory getParent() {
		return parent
    }

    void setParent(ProductCategory parent) {
		this.parent = parent
    }

    List<ProductCategory> getSubcategories() {
		return subcategories
    }

    void setSubcategories(List<ProductCategory> subcategories) {
		this.subcategories = subcategories
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getDescription() {
		return description
    }

    void setDescription(String description) {
		this.description = description
    }

    void sync(tools.dynamia.cms.products.dto.ProductCategoryDTO dto) {
		name = dto.name
        order = dto.order
        alternateName = dto.alternateName
        active = dto.active
        description = dto.description
        externalRef = dto.externalRef

    }

	@Override
    String toString() {
		return name
    }

}
