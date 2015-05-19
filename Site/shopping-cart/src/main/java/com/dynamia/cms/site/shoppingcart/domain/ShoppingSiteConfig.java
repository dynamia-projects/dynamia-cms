package com.dynamia.cms.site.shoppingcart.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.mail.domain.MailAccount;
import com.dynamia.cms.site.mail.domain.MailTemplate;
import com.dynamia.tools.domain.SimpleEntity;

@Entity
@Table(name = "sc_configuration")
public class ShoppingSiteConfig extends SimpleEntity implements SiteAware {

	@NotNull
	@OneToOne
	private Site site;
	private String paymentGatewayId;
	private String paymentGatewayName;
	private boolean paymentEnabled;
	private BigDecimal minPaymentAmount;
	private String defaultCurrency;
	private String descriptionTemplate;
	private String notificationEmails;
	@OneToOne
	private MailTemplate orderCompletedMailTemplate;
	@OneToOne
	private MailTemplate orderShippedMailTemplate;
	@OneToOne
	private MailTemplate notificationMailTemplate;
	@OneToOne
	private MailAccount mailAccount;

	public MailAccount getMailAccount() {
		return mailAccount;
	}

	public void setMailAccount(MailAccount mailAccount) {
		this.mailAccount = mailAccount;
	}

	public MailTemplate getOrderCompletedMailTemplate() {
		return orderCompletedMailTemplate;
	}

	public void setOrderCompletedMailTemplate(MailTemplate orderCompletedMailTemplate) {
		this.orderCompletedMailTemplate = orderCompletedMailTemplate;
	}

	public MailTemplate getOrderShippedMailTemplate() {
		return orderShippedMailTemplate;
	}

	public void setOrderShippedMailTemplate(MailTemplate orderShippedMailTemplate) {
		this.orderShippedMailTemplate = orderShippedMailTemplate;
	}

	public MailTemplate getNotificationMailTemplate() {
		return notificationMailTemplate;
	}

	public void setNotificationMailTemplate(MailTemplate notificationMailTemplate) {
		this.notificationMailTemplate = notificationMailTemplate;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getDescriptionTemplate() {
		return descriptionTemplate;
	}

	public String getNotificationEmails() {
		return notificationEmails;
	}

	public void setNotificationEmails(String notificationEmails) {
		this.notificationEmails = notificationEmails;
	}

	public void setDescriptionTemplate(String descriptionTemplate) {
		this.descriptionTemplate = descriptionTemplate;
	}

	public String getPaymentGatewayId() {
		return paymentGatewayId;
	}

	public void setPaymentGatewayId(String paymentGatewayId) {
		this.paymentGatewayId = paymentGatewayId;
	}

	public String getPaymentGatewayName() {
		return paymentGatewayName;
	}

	public void setPaymentGatewayName(String paymentGatewayName) {
		this.paymentGatewayName = paymentGatewayName;
	}

	public boolean isPaymentEnabled() {
		return paymentEnabled;
	}

	public void setPaymentEnabled(boolean paymentEnabled) {
		this.paymentEnabled = paymentEnabled;
	}

	public BigDecimal getMinPaymentAmount() {
		return minPaymentAmount;
	}

	public void setMinPaymentAmount(BigDecimal minPaymentAmount) {
		this.minPaymentAmount = minPaymentAmount;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

}
