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

package toosl.dynamia.cms.shoppingcart.dto

class OrderStatusDetailDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8443711900361650187L
    private String code
    private String description
    private double quantity
    private BigDecimal price
    private BigDecimal total
    private Date shipDate
    private String currency
    private BigDecimal priceVAT
    private BigDecimal vat
    private double vatPercent
    private BigDecimal totalVAT

    String getCode() {
		return code
    }

    void setCode(String code) {
		this.code = code
    }

    String getDescription() {
		return description
    }

    void setDescription(String description) {
		this.description = description
    }

    double getQuantity() {
		return quantity
    }

    void setQuantity(double quantity) {
		this.quantity = quantity
    }

    BigDecimal getPrice() {
		return price
    }

    void setPrice(BigDecimal price) {
		this.price = price
    }

    BigDecimal getTotal() {
		return total
    }

    void setTotal(BigDecimal total) {
		this.total = total
    }

    Date getShipDate() {
		return shipDate
    }

    void setShipDate(Date shipDate) {
		this.shipDate = shipDate
    }

    String getCurrency() {
		return currency
    }

    void setCurrency(String currency) {
		this.currency = currency
    }

    BigDecimal getPriceVAT() {
		return priceVAT
    }

    void setPriceVAT(BigDecimal priceVAT) {
		this.priceVAT = priceVAT
    }

    BigDecimal getVat() {
		return vat
    }

    void setVat(BigDecimal vat) {
		this.vat = vat
    }

    double getVatPercent() {
		return vatPercent
    }

    void setVatPercent(double vatPercent) {
		this.vatPercent = vatPercent
    }

    BigDecimal getTotalVAT() {
		return totalVAT
    }

    void setTotalVAT(BigDecimal totalVAT) {
		this.totalVAT = totalVAT
    }

}
