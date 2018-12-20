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
