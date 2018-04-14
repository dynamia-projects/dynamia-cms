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
class ProductCreditPriceDTO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5111921265189786308L
    private ProductDTO product
    private int number
    private String description
    private BigDecimal price

    ProductDTO getProduct() {
        return product
    }

    void setProduct(ProductDTO product) {
        this.product = product
    }

    int getNumber() {
        return number
    }

    void setNumber(int number) {
        this.number = number
    }

    String getDescription() {
        return description
    }

    void setDescription(String description) {
        this.description = description
    }

    BigDecimal getPrice() {
        return price
    }

    void setPrice(BigDecimal price) {
        this.price = price
    }

}
