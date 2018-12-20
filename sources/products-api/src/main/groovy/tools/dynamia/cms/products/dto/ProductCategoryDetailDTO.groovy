/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
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
