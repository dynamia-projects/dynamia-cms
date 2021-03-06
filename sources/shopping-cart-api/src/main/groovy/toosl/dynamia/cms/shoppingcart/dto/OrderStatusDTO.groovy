/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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

class OrderStatusDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2477969153960894112L

    private String customerCode
    private String customerNum
    private String customerName
    private String customerGroup
    private String customerDocType
    private BigDecimal customerCredit

    private String paymentCondition
    private String id
    private String number

    private Date date
    private Date dueDate
    private BigDecimal total
    private BigDecimal paid
    private BigDecimal balance
    private int days
    private String type
    private List<OrderStatusDetailDTO> details = new ArrayList<>()

    List<OrderStatusDetailDTO> getDetails() {
		return details
    }

    void setDetails(List<OrderStatusDetailDTO> details) {
		this.details = details
    }

    String getId() {
		return id
    }

    void setId(String id) {
		this.id = id
    }

    String getType() {
		return type
    }

    void setType(String type) {
		this.type = type
    }

    String getCustomerDocType() {
		return customerDocType
    }

    void setCustomerDocType(String customerDocType) {
		this.customerDocType = customerDocType
    }

    String getCustomerCode() {
		return customerCode
    }

    void setCustomerCode(String customerCode) {
		this.customerCode = customerCode
    }

    String getCustomerNum() {
		return customerNum
    }

    void setCustomerNum(String customerNum) {
		this.customerNum = customerNum
    }

    String getCustomerName() {
		return customerName
    }

    void setCustomerName(String customerName) {
		this.customerName = customerName
    }

    String getCustomerGroup() {
		return customerGroup
    }

    void setCustomerGroup(String customerGroup) {
		this.customerGroup = customerGroup
    }

    BigDecimal getCustomerCredit() {
		return customerCredit
    }

    void setCustomerCredit(BigDecimal customerCredit) {
		this.customerCredit = customerCredit
    }

    String getPaymentCondition() {
		return paymentCondition
    }

    void setPaymentCondition(String paymentCondition) {
		this.paymentCondition = paymentCondition
    }

    String getNumber() {
		return number
    }

    void setNumber(String number) {
		this.number = number
    }

    Date getDate() {
		return date
    }

    void setDate(Date date) {
		this.date = date
    }

    Date getDueDate() {
		return dueDate
    }

    void setDueDate(Date dueDate) {
		this.dueDate = dueDate
    }

    BigDecimal getTotal() {
		return total
    }

    void setTotal(BigDecimal total) {
		this.total = total
    }

    BigDecimal getPaid() {
		return paid
    }

    void setPaid(BigDecimal paid) {
		this.paid = paid
    }

    BigDecimal getBalance() {
		return balance
    }

    void setBalance(BigDecimal balance) {
		this.balance = balance
    }

    int getDays() {
		return days
    }

    void setDays(int days) {
		this.days = days
    }

}
