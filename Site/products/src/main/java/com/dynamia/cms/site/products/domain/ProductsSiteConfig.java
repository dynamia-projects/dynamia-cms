/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.mail.domain.MailAccount;
import com.dynamia.cms.site.mail.domain.MailTemplate;

import tools.dynamia.domain.SimpleEntity;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "prd_sites_config")
public class ProductsSiteConfig extends SimpleEntity implements SiteAware {

	@OneToOne
	@NotNull
	@JoinColumn(unique = true)
	private Site site;

	private String pricePattern;
	private String defaultCurrency;

	private String datasourceURL;
	private String datasourceUsername;
	private String datasourcePassword;
	private String datasourceImagesURL;
	private String datasourceBrandImagesURL;
	private String datasourceStoreImagesURL;

	private String token;

	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date lastSync;

	private int productsPerPage;

	private int productsStockToShow;

	private boolean shopEnabled;

	private boolean quoteEnabled;

	private boolean showBadges;

	private boolean synchronizationEnabled;

	@OneToOne
	private MailAccount mailAccount;
	@OneToOne
	private MailTemplate shareProductMailTemplate;
	@OneToOne
	private MailTemplate orderCompletedMailTemplate;

	@OneToMany(mappedBy = "siteConfig", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ProductsSiteConfigParameter> parameters = new ArrayList<>();

	private int minStock;

	public int getMinStock() {
		return minStock;
	}

	public void setMinStock(int minStock) {
		this.minStock = minStock;
	}

	public boolean isSynchronizationEnabled() {
		return synchronizationEnabled;
	}

	public void setSynchronizationEnabled(boolean synchronizationEnabled) {
		this.synchronizationEnabled = synchronizationEnabled;
	}

	public boolean isShowBadges() {
		return showBadges;
	}

	public void setShowBadges(boolean showBadges) {
		this.showBadges = showBadges;
	}

	public MailAccount getMailAccount() {
		return mailAccount;
	}

	public void setMailAccount(MailAccount mailAccount) {
		this.mailAccount = mailAccount;
	}

	public MailTemplate getShareProductMailTemplate() {
		return shareProductMailTemplate;
	}

	public void setShareProductMailTemplate(MailTemplate shareProductMailTemplate) {
		this.shareProductMailTemplate = shareProductMailTemplate;
	}

	public MailTemplate getOrderCompletedMailTemplate() {
		return orderCompletedMailTemplate;
	}

	public void setOrderCompletedMailTemplate(MailTemplate orderCompletedMailTemplate) {
		this.orderCompletedMailTemplate = orderCompletedMailTemplate;
	}

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void setSite(Site site) {
		this.site = site;
	}

	public List<ProductsSiteConfigParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<ProductsSiteConfigParameter> parameters) {
		this.parameters = parameters;
	}

	public boolean isShopEnabled() {
		return shopEnabled;
	}

	public void setShopEnabled(boolean shopEnabled) {
		this.shopEnabled = shopEnabled;
	}

	public boolean isQuoteEnabled() {
		return quoteEnabled;
	}

	public void setQuoteEnabled(boolean quoteEnabled) {
		this.quoteEnabled = quoteEnabled;
	}

	public int getProductsPerPage() {
		if (productsPerPage <= 0) {
			productsPerPage = 16;
		}
		return productsPerPage;
	}

	public void setProductsPerPage(int productsPerPage) {
		this.productsPerPage = productsPerPage;
	}

	public int getProductsStockToShow() {
		if (productsStockToShow <= 0) {
			productsStockToShow = 5;
		}
		return productsStockToShow;
	}

	public void setProductsStockToShow(int productsStockToShow) {
		this.productsStockToShow = productsStockToShow;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public Date getLastSync() {
		return lastSync;
	}

	public void setLastSync(Date lastSync) {
		this.lastSync = lastSync;
	}

	public String getDatasourceURL() {
		return datasourceURL;
	}

	public void setDatasourceURL(String datasourceURL) {
		this.datasourceURL = datasourceURL;
	}

	public String getDatasourceUsername() {
		return datasourceUsername;
	}

	public void setDatasourceUsername(String datasourceUsername) {
		this.datasourceUsername = datasourceUsername;
	}

	public String getDatasourcePassword() {
		return datasourcePassword;
	}

	public void setDatasourcePassword(String datasourcePassword) {
		this.datasourcePassword = datasourcePassword;
	}

	public String getDatasourceImagesURL() {
		return datasourceImagesURL;
	}

	public void setDatasourceImagesURL(String datasourceImagesURL) {
		this.datasourceImagesURL = datasourceImagesURL;
	}

	public String getDatasourceBrandImagesURL() {
		return datasourceBrandImagesURL;
	}

	public void setDatasourceBrandImagesURL(String datasourceBrandImagesURL) {
		this.datasourceBrandImagesURL = datasourceBrandImagesURL;
	}

	public String getDatasourceStoreImagesURL() {
		return datasourceStoreImagesURL;
	}

	public void setDatasourceStoreImagesURL(String datasourceStoreImagesURL) {
		this.datasourceStoreImagesURL = datasourceStoreImagesURL;
	}

	public String getPricePattern() {
		return pricePattern;
	}

	public void setPricePattern(String pricePattern) {
		this.pricePattern = pricePattern;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Map<String, String> getParametersAsMap() {
		Map<String, String> params = new HashMap<>();
		for (ProductsSiteConfigParameter param : getParameters()) {
			params.put(param.getName(), param.getValue());
		}
		return params;
	}

}
