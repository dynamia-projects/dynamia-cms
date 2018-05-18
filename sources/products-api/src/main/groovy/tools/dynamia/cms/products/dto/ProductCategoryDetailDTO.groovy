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
package tools.dynamia.cms.products.dto
/**
 *
 * @author Mario Serrano Leones
 */
class ProductCategoryDetailDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7113328659044071865L

    private String name

    private String values
    private Long externalRef
    private int order
    private boolean filterable

    private ProductCategoryDTO category

    boolean isFilterable() {
		return filterable
    }

    void setFilterable(boolean filterable) {
		this.filterable = filterable
    }

    int getOrder() {
		return order
    }

    void setOrder(int order) {
		this.order = order
    }

    ProductCategoryDTO getCategory() {
		return category
    }

    void setCategory(ProductCategoryDTO category) {
		this.category = category
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

    String getValues() {
		return values
    }

    void setValues(String values) {
		this.values = values
    }

}
