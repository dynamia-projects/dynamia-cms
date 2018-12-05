/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
