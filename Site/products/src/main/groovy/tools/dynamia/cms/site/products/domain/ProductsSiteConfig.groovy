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
package tools.dynamia.cms.site.products.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.mail.domain.MailAccount
import tools.dynamia.cms.site.mail.domain.MailTemplate
import tools.dynamia.domain.SimpleEntity

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_sites_config")
class ProductsSiteConfig extends SimpleEntity implements SiteAware {

	@OneToOne
	@NotNull
	@JoinColumn(unique = true)
	private Site site

    private String pricePattern = ""
    private String defaultCurrency = ""

    @JsonIgnore
	private String datasourceURL
    @JsonIgnore
	private String datasourceUsername
    @JsonIgnore
	private String datasourcePassword
    @JsonIgnore
	private String datasourceImagesURL
    @JsonIgnore
	private String datasourceBrandImagesURL
    @JsonIgnore
	private String datasourceStoreImagesURL
    @JsonIgnore
	private String datasourceStoreContactImagesURL

    @JsonIgnore
	private String token

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date lastSync

    private int productsPerPage

    private int productsStockToShow

    private boolean shopEnabled

    private boolean quoteEnabled

    private boolean showBadges

    private boolean synchronizationEnabled

    @OneToOne
	@JsonIgnore
	private MailAccount mailAccount
    @OneToOne
	@JsonIgnore
	private MailTemplate shareProductMailTemplate
    @OneToOne
	@JsonIgnore
	private MailTemplate orderCompletedMailTemplate

    @OneToMany(mappedBy = "siteConfig", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private List<ProductsSiteConfigParameter> parameters = new ArrayList<>()
    @JsonIgnore
	private int minStock
    @JsonIgnore
	private boolean syncStockDetails
    @JsonIgnore
	private boolean syncProductDetails
    @JsonIgnore
	private boolean syncProductImages
    @JsonIgnore
	private boolean syncProductCreditPrices
    @JsonIgnore
	private boolean syncStoreContacts

    @Column(length = 4000)
	@JsonIgnore
	private String priceUserGroup
    @JsonIgnore
	@Column(length = 4000)
	private String price2UserGroup
    @JsonIgnore
	@Column(length = 4000)
	private String costUserGroup

    @JsonIgnore
	private String reviewsConnectorURL

    String getReviewsConnectorURL() {
		return reviewsConnectorURL
    }

    void setReviewsConnectorURL(String reviewsConnectorURL) {
		this.reviewsConnectorURL = reviewsConnectorURL
    }

    String getDatasourceStoreContactImagesURL() {
		return datasourceStoreContactImagesURL
    }

    void setDatasourceStoreContactImagesURL(String datasourceStoreContactImagesURL) {
		this.datasourceStoreContactImagesURL = datasourceStoreContactImagesURL
    }

    boolean isSyncStoreContacts() {
		return syncStoreContacts
    }

    void setSyncStoreContacts(boolean syncStoreContacts) {
		this.syncStoreContacts = syncStoreContacts
    }

    String getPriceUserGroup() {
		return priceUserGroup
    }

    void setPriceUserGroup(String priceUserGroup) {
		this.priceUserGroup = priceUserGroup
    }

    String getPrice2UserGroup() {
		return price2UserGroup
    }

    void setPrice2UserGroup(String price2UserGroup) {
		this.price2UserGroup = price2UserGroup
    }

    String getCostUserGroup() {
		return costUserGroup
    }

    void setCostUserGroup(String costUserGroup) {
		this.costUserGroup = costUserGroup
    }

    boolean isSyncProductCreditPrices() {
		return syncProductCreditPrices
    }

    void setSyncProductCreditPrices(boolean syncProductCreditPrices) {
		this.syncProductCreditPrices = syncProductCreditPrices
    }

    boolean isSyncStockDetails() {
		return syncStockDetails
    }

    void setSyncStockDetails(boolean syncStockDetails) {
		this.syncStockDetails = syncStockDetails
    }

    boolean isSyncProductDetails() {
		return syncProductDetails
    }

    void setSyncProductDetails(boolean syncProductDetails) {
		this.syncProductDetails = syncProductDetails
    }

    boolean isSyncProductImages() {
		return syncProductImages
    }

    void setSyncProductImages(boolean syncProductImages) {
		this.syncProductImages = syncProductImages
    }

    int getMinStock() {
		return minStock
    }

    void setMinStock(int minStock) {
		this.minStock = minStock
    }

    boolean isSynchronizationEnabled() {
		return synchronizationEnabled
    }

    void setSynchronizationEnabled(boolean synchronizationEnabled) {
		this.synchronizationEnabled = synchronizationEnabled
    }

    boolean isShowBadges() {
		return showBadges
    }

    void setShowBadges(boolean showBadges) {
		this.showBadges = showBadges
    }

    MailAccount getMailAccount() {
		return mailAccount
    }

    void setMailAccount(MailAccount mailAccount) {
		this.mailAccount = mailAccount
    }

    MailTemplate getShareProductMailTemplate() {
		return shareProductMailTemplate
    }

    void setShareProductMailTemplate(MailTemplate shareProductMailTemplate) {
		this.shareProductMailTemplate = shareProductMailTemplate
    }

    MailTemplate getOrderCompletedMailTemplate() {
		return orderCompletedMailTemplate
    }

    void setOrderCompletedMailTemplate(MailTemplate orderCompletedMailTemplate) {
		this.orderCompletedMailTemplate = orderCompletedMailTemplate
    }

	@Override
    Site getSite() {
		return site
    }

	@Override
    void setSite(Site site) {
		this.site = site
    }

    List<ProductsSiteConfigParameter> getParameters() {
		return parameters
    }

    void setParameters(List<ProductsSiteConfigParameter> parameters) {
		this.parameters = parameters
    }

    boolean isShopEnabled() {
		return shopEnabled
    }

    void setShopEnabled(boolean shopEnabled) {
		this.shopEnabled = shopEnabled
    }

    boolean isQuoteEnabled() {
		return quoteEnabled
    }

    void setQuoteEnabled(boolean quoteEnabled) {
		this.quoteEnabled = quoteEnabled
    }

    int getProductsPerPage() {
		if (productsPerPage <= 0) {
			productsPerPage = 16
        }
		return productsPerPage
    }

    void setProductsPerPage(int productsPerPage) {
		this.productsPerPage = productsPerPage
    }

    int getProductsStockToShow() {
		if (productsStockToShow <= 0) {
			productsStockToShow = 5
        }
		return productsStockToShow
    }

    void setProductsStockToShow(int productsStockToShow) {
		this.productsStockToShow = productsStockToShow
    }

    String getDefaultCurrency() {
		return defaultCurrency
    }

    void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency
    }

    Date getLastSync() {
		return lastSync
    }

    void setLastSync(Date lastSync) {
		this.lastSync = lastSync
    }

    String getDatasourceURL() {
		return datasourceURL
    }

    void setDatasourceURL(String datasourceURL) {
		this.datasourceURL = datasourceURL
    }

    String getDatasourceUsername() {
		return datasourceUsername
    }

    void setDatasourceUsername(String datasourceUsername) {
		this.datasourceUsername = datasourceUsername
    }

    String getDatasourcePassword() {
		return datasourcePassword
    }

    void setDatasourcePassword(String datasourcePassword) {
		this.datasourcePassword = datasourcePassword
    }

    String getDatasourceImagesURL() {
		return datasourceImagesURL
    }

    void setDatasourceImagesURL(String datasourceImagesURL) {
		this.datasourceImagesURL = datasourceImagesURL
    }

    String getDatasourceBrandImagesURL() {
		return datasourceBrandImagesURL
    }

    void setDatasourceBrandImagesURL(String datasourceBrandImagesURL) {
		this.datasourceBrandImagesURL = datasourceBrandImagesURL
    }

    String getDatasourceStoreImagesURL() {
		return datasourceStoreImagesURL
    }

    void setDatasourceStoreImagesURL(String datasourceStoreImagesURL) {
		this.datasourceStoreImagesURL = datasourceStoreImagesURL
    }

    String getPricePattern() {
		return pricePattern
    }

    void setPricePattern(String pricePattern) {
		this.pricePattern = pricePattern
    }

    String getToken() {
		return token
    }

    void setToken(String token) {
		this.token = token
    }

	@JsonIgnore
    Map<String, String> getParametersAsMap() {
		Map<String, String> params = new HashMap<>()
        for (ProductsSiteConfigParameter param : getParameters()) {
			params.put(param.getName(), param.getValue())
        }
		return params
    }

}
