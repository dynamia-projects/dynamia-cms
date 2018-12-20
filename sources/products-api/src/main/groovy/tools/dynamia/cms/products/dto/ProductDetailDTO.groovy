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
class ProductDetailDTO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1265515027309763424L
    private ProductDTO product
    private String name

    private String value
    private String description
    private Long externalRef
    private int order
    private String imageURL
    private boolean featured
    private boolean filterable
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

    ProductDTO getProduct() {
		return product
    }

    void setProduct(ProductDTO product) {
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
	
	

}
