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
package tools.dynamia.cms.site.products.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.site.core.Orderable
import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.products.dto.ProductDetailDTO
import tools.dynamia.domain.SimpleEntity

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_details")
class ProductDetail extends SimpleEntity implements SiteAware, Orderable {

	@OneToOne
	@NotNull
	@JsonIgnore
	private Site site

    @ManyToOne
	@JsonIgnore
	private Product product
    private String name
    @Column(name = "detvalue")
	private String value
    private String description
    private Long externalRef
    @Column(name = "detorder")
	private int order
    private String imageURL
    private boolean featured
    private boolean filterale
    private String value2
    private String color
    private String url
    private String url2

    String getValue2() {
		return value2
    }

    void setValue2(String value2) {
		this.value2 = value2
    }

    String getColor() {
		return color
    }

    void setColor(String color) {
		this.color = color
    }

    String getUrl() {
		return url
    }

    void setUrl(String url) {
		this.url = url
    }

    String getUrl2() {
		return url2
    }

    void setUrl2(String url2) {
		this.url2 = url2
    }

    boolean isFilterale() {
		return filterale
    }

    void setFilterale(boolean filterale) {
		this.filterale = filterale
    }

    String getImageURL() {
		return imageURL
    }

    void setImageURL(String imageURL) {
		this.imageURL = imageURL
    }

    boolean isFeatured() {
		return featured
    }

    void setFeatured(boolean featured) {
		this.featured = featured
    }

    int getOrder() {
		return order
    }

    void setOrder(int order) {
		this.order = order
    }

    Site getSite() {
		return site
    }

    void setSite(Site site) {
		this.site = site
    }

    Product getProduct() {
		return product
    }

    void setProduct(Product product) {
		this.product = product
    }

    Long getExternalRef() {
		return externalRef
    }

    void setExternalRef(Long externalRef) {
		this.externalRef = externalRef
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getValue() {
		return value
    }

    void setValue(String value) {
		this.value = value
    }

    String getDescription() {
		return description
    }

    void setDescription(String description) {
		this.description = description
    }

    void sync(ProductDetailDTO dto) {
		description = dto.getDescription()
        externalRef = dto.getExternalRef()
        name = dto.getName()
        value = dto.getValue()
        order = dto.getOrder()
        imageURL = dto.getImageURL()
        featured = dto.isFeatured()
        filterale = dto.isFilterable()
        value2 = dto.getValue2()
        url = dto.getUrl()
        url2 = dto.getUrl2()
        color = dto.getColor()
    }

}
