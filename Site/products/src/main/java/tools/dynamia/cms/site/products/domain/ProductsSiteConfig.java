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
package tools.dynamia.cms.site.products.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.mail.domain.MailAccount;
import tools.dynamia.cms.site.mail.domain.MailTemplate;

import tools.dynamia.domain.SimpleEntity;

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_sites_config")
public class ProductsSiteConfig extends SimpleEntity implements SiteAware {

	@OneToOne
	@NotNull
	@JoinColumn(unique = true)
	private Site site;

	private String pricePattern = "";
	private String defaultCurrency = "";

	@JsonIgnore
	private String datasourceURL;
	@JsonIgnore
	private String datasourceUsername;
	@JsonIgnore
	private String datasourcePassword;
	@JsonIgnore
	private String datasourceImagesURL;
	@JsonIgnore
	private String datasourceBrandImagesURL;
	@JsonIgnore
	private String datasourceStoreImagesURL;
	@JsonIgnore
	private String datasourceStoreContactImagesURL;
	@JsonIgnore
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
	@JsonIgnore
	private MailAccount mailAccount;
	@OneToOne
	@JsonIgnore
	private MailTemplate shareProductMailTemplate;
	@OneToOne
	@JsonIgnore
	private MailTemplate orderCompletedMailTemplate;

	@OneToMany(mappedBy = "siteConfig", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private List<ProductsSiteConfigParameter> parameters = new ArrayList<>();
	@JsonIgnore
	private int minStock;
	@JsonIgnore
	private boolean syncStockDetails;
	@JsonIgnore
	private boolean syncProductDetails;
	@JsonIgnore
	private boolean syncProductImages;
	@JsonIgnore
	private boolean syncProductCreditPrices;
	@JsonIgnore
	private boolean syncStoreContacts;

	@Column(length = 4000)
	@JsonIgnore
	private String priceUserGroup;
	@JsonIgnore
	@Column(length = 4000)
	private String price2UserGroup;
	@JsonIgnore
	@Column(length = 4000)
	private String costUserGroup;

	public String getDatasourceStoreContactImagesURL() {
		return datasourceStoreContactImagesURL;
	}

	public void setDatasourceStoreContactImagesURL(String datasourceStoreContactImagesURL) {
		this.datasourceStoreContactImagesURL = datasourceStoreContactImagesURL;
	}

	public boolean isSyncStoreContacts() {
		return syncStoreContacts;
	}

	public void setSyncStoreContacts(boolean syncStoreContacts) {
		this.syncStoreContacts = syncStoreContacts;
	}

	public String getPriceUserGroup() {
		return priceUserGroup;
	}

	public void setPriceUserGroup(String priceUserGroup) {
		this.priceUserGroup = priceUserGroup;
	}

	public String getPrice2UserGroup() {
		return price2UserGroup;
	}

	public void setPrice2UserGroup(String price2UserGroup) {
		this.price2UserGroup = price2UserGroup;
	}

	public String getCostUserGroup() {
		return costUserGroup;
	}

	public void setCostUserGroup(String costUserGroup) {
		this.costUserGroup = costUserGroup;
	}

	public boolean isSyncProductCreditPrices() {
		return syncProductCreditPrices;
	}

	public void setSyncProductCreditPrices(boolean syncProductCreditPrices) {
		this.syncProductCreditPrices = syncProductCreditPrices;
	}

	public boolean isSyncStockDetails() {
		return syncStockDetails;
	}

	public void setSyncStockDetails(boolean syncStockDetails) {
		this.syncStockDetails = syncStockDetails;
	}

	public boolean isSyncProductDetails() {
		return syncProductDetails;
	}

	public void setSyncProductDetails(boolean syncProductDetails) {
		this.syncProductDetails = syncProductDetails;
	}

	public boolean isSyncProductImages() {
		return syncProductImages;
	}

	public void setSyncProductImages(boolean syncProductImages) {
		this.syncProductImages = syncProductImages;
	}

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

	@JsonIgnore
	public Map<String, String> getParametersAsMap() {
		Map<String, String> params = new HashMap<>();
		for (ProductsSiteConfigParameter param : getParameters()) {
			params.put(param.getName(), param.getValue());
		}
		return params;
	}

}
