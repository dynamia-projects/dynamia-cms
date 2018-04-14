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
package tools.dynamia.cms.site.products.dto
/**
 *
 * @author Mario Serrano Leones
 */
class ProductCategoryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6391246496268751494L

    private ProductCategoryDTO parent

    private List<ProductCategoryDTO> subcategories = new ArrayList<>()

    private String name
    private String alternateName
    private String description
    private boolean active
    private Long externalRef
    private Long relatedCategoryExternalRef
    private int order

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

	private List<ProductCategoryDetailDTO> details = new ArrayList<>()

    Long getRelatedCategoryExternalRef() {
		return relatedCategoryExternalRef
    }

    void setRelatedCategoryExternalRef(Long relatedCategoryExternalRef) {
		this.relatedCategoryExternalRef = relatedCategoryExternalRef
    }

    List<ProductCategoryDetailDTO> getDetails() {
		return details
    }

    void setDetails(List<ProductCategoryDetailDTO> details) {
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

    ProductCategoryDTO getParent() {
		return parent
    }

    void setParent(ProductCategoryDTO parent) {
		this.parent = parent
    }

    List<ProductCategoryDTO> getSubcategories() {
		return subcategories
    }

    void setSubcategories(List<ProductCategoryDTO> subcategories) {
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

}
