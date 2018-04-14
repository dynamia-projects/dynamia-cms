package toosl.dynamia.cms.site.shoppingcart.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderStatusDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2477969153960894112L;

	private String customerCode;
	private String customerNum;
	private String customerName;
	private String customerGroup;
	private String customerDocType;
	private BigDecimal customerCredit;

	private String paymentCondition;
	private String id;
	private String number;

	private Date date;
	private Date dueDate;
	private BigDecimal total;
	private BigDecimal paid;
	private BigDecimal balance;
	private int days;
	private String type;
	private List<OrderStatusDetailDTO> details = new ArrayList<>();

	public List<OrderStatusDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<OrderStatusDetailDTO> details) {
		this.details = details;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCustomerDocType() {
		return customerDocType;
	}

	public void setCustomerDocType(String customerDocType) {
		this.customerDocType = customerDocType;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerNum() {
		return customerNum;
	}

	public void setCustomerNum(String customerNum) {
		this.customerNum = customerNum;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(String customerGroup) {
		this.customerGroup = customerGroup;
	}

	public BigDecimal getCustomerCredit() {
		return customerCredit;
	}

	public void setCustomerCredit(BigDecimal customerCredit) {
		this.customerCredit = customerCredit;
	}

	public String getPaymentCondition() {
		return paymentCondition;
	}

	public void setPaymentCondition(String paymentCondition) {
		this.paymentCondition = paymentCondition;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getPaid() {
		return paid;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

}
