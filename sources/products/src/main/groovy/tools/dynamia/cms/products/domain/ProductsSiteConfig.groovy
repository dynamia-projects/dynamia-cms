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
package tools.dynamia.cms.products.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.mail.domain.MailAccount
import tools.dynamia.cms.mail.domain.MailTemplate
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
        for (ProductsSiteConfigParameter param : (parameters)) {
			params.put(param.name, param.value)
        }
		return params
    }

}
