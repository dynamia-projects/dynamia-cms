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
package tools.dynamia.cms.products.dto

import tools.dynamia.cms.products.api.ValueType

/**
 * @author Mario Serrano Leones
 */
class ProductDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1577777821132625315L

    private String name

    private String description

    private String longDescription

    private String sku
    private String reference
    private Long externalRef

    private BigDecimal price
    private BigDecimal price2
    private BigDecimal cost
    private BigDecimal lastPrice
    private BigDecimal storePrice
    private String priceDescription
    private long stock
    private boolean active
    private boolean featured
    private boolean sale
    private boolean newproduct
    private String status
    private String image
    private String image2
    private String image3
    private String image4
    private String videoURL
    private String videoURL2
    private Date creationDate
    private String tags
    private ProductCategoryDTO category
    private ProductBrandDTO brand

    private List<ProductDetailDTO> details = new ArrayList<>()

    private List<ProductStockDTO> stockDetails = new ArrayList<>()

    private List<ProductCreditPriceDTO> creditPrices = new ArrayList<>()

    private String externalLink

    private boolean showCreditPrices
    private boolean showLastPrice
    private boolean sellInStore = true
    private boolean sellInWeb = true

    // Promotion config
    private boolean promoEnabled
    private BigDecimal promoValue
    private ValueType promoValueType = ValueType.PERCENT
    private Date promoStartDate
    private Date promoEndDate
    private String promoName

    private String taxName
    private double taxPercent
    private boolean taxable
    private boolean taxIncluded
    private String unit
    private String quality
    private String currency

    boolean isTaxIncluded() {
        return taxIncluded
    }

    void setTaxIncluded(boolean taxIncluded) {
        this.taxIncluded = taxIncluded
    }

    String getTaxName() {
        return taxName
    }

    void setTaxName(String taxName) {
        this.taxName = taxName
    }

    double getTaxPercent() {
        return taxPercent
    }

    void setTaxPercent(double taxPercent) {
        this.taxPercent = taxPercent
    }

    boolean isTaxable() {
        return taxable
    }

    void setTaxable(boolean taxable) {
        this.taxable = taxable
    }

    String getUnit() {
        return unit
    }

    void setUnit(String unit) {
        this.unit = unit
    }

    String getVideoURL2() {
        return videoURL2
    }

    void setVideoURL2(String videoURL2) {
        this.videoURL2 = videoURL2
    }

    BigDecimal getStorePrice() {
        return storePrice
    }

    void setStorePrice(BigDecimal storePrice) {
        this.storePrice = storePrice
    }

    String getVideoURL() {
        return videoURL
    }

    void setVideoURL(String videoURL) {
        this.videoURL = videoURL
    }

    boolean isShowLastPrice() {
        return showLastPrice
    }

    void setShowLastPrice(boolean showLastPrice) {
        this.showLastPrice = showLastPrice
    }

    String getReference() {
        return reference
    }

    void setReference(String reference) {
        this.reference = reference
    }

    String getPriceDescription() {
        return priceDescription
    }

    void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription
    }

    boolean isShowCreditPrices() {
        return showCreditPrices
    }

    void setShowCreditPrices(boolean showCreditPrices) {
        this.showCreditPrices = showCreditPrices
    }

    List<ProductCreditPriceDTO> getCreditPrices() {
        return creditPrices
    }

    void setCreditPrices(List<ProductCreditPriceDTO> creditPrices) {
        this.creditPrices = creditPrices
    }

    boolean isNewproduct() {
        return newproduct
    }

    void setNewproduct(boolean newproduct) {
        this.newproduct = newproduct
    }

    String getExternalLink() {
        return externalLink
    }

    void setExternalLink(String externalLink) {
        this.externalLink = externalLink
    }

    String getStatus() {
        return status
    }

    void setStatus(String status) {
        this.status = status
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getDescription() {
        return description
    }

    void setDescription(String description) {
        this.description = description
    }

    String getLongDescription() {
        return longDescription
    }

    void setLongDescription(String longDescription) {
        this.longDescription = longDescription
    }

    String getSku() {
        return sku
    }

    void setSku(String sku) {
        this.sku = sku
    }

    Long getExternalRef() {
        return externalRef
    }

    void setExternalRef(Long externalRef) {
        this.externalRef = externalRef
    }

    BigDecimal getPrice() {
        return price
    }

    void setPrice(BigDecimal price) {
        this.price = price
    }

    BigDecimal getLastPrice() {
        return lastPrice
    }

    void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice
    }

    long getStock() {
        return stock
    }

    void setStock(long stock) {
        this.stock = stock
    }

    boolean isActive() {
        return active
    }

    void setActive(boolean active) {
        this.active = active
    }

    boolean isFeatured() {
        return featured
    }

    void setFeatured(boolean featured) {
        this.featured = featured
    }

    boolean isSale() {
        return sale
    }

    void setSale(boolean sale) {
        this.sale = sale
    }

    String getImage() {
        return image
    }

    void setImage(String image) {
        this.image = image
    }

    String getImage2() {
        return image2
    }

    void setImage2(String image2) {
        this.image2 = image2
    }

    String getImage3() {
        return image3
    }

    void setImage3(String image3) {
        this.image3 = image3
    }

    String getImage4() {
        return image4
    }

    void setImage4(String image4) {
        this.image4 = image4
    }

    ProductCategoryDTO getCategory() {
        return category
    }

    void setCategory(ProductCategoryDTO category) {
        this.category = category
    }

    ProductBrandDTO getBrand() {
        return brand
    }

    void setBrand(ProductBrandDTO brand) {
        this.brand = brand
    }

    String getTags() {
        return tags
    }

    void setTags(String tags) {
        this.tags = tags
    }

    List<ProductDetailDTO> getDetails() {
        return details
    }

    void setDetails(List<ProductDetailDTO> details) {
        this.details = details
    }

    List<ProductStockDTO> getStockDetails() {
        return stockDetails
    }

    void setStockDetails(List<ProductStockDTO> stockDetails) {
        this.stockDetails = stockDetails
    }

    boolean isSellInStore() {
        return sellInStore
    }

    void setSellInStore(boolean sellInStore) {
        this.sellInStore = sellInStore
    }

    boolean isSellInWeb() {
        return sellInWeb
    }

    void setSellInWeb(boolean sellInWeb) {
        this.sellInWeb = sellInWeb
    }

    boolean isPromoEnabled() {
        return promoEnabled
    }

    void setPromoEnabled(boolean promoEnabled) {
        this.promoEnabled = promoEnabled
    }

    BigDecimal getPromoValue() {
        return promoValue
    }

    void setPromoValue(BigDecimal promoValue) {
        this.promoValue = promoValue
    }

    ValueType getPromoValueType() {
        return promoValueType
    }

    void setPromoValueType(ValueType promoValueType) {
        this.promoValueType = promoValueType
    }

    Date getPromoStartDate() {
        return promoStartDate
    }

    void setPromoStartDate(Date promoStartDate) {
        this.promoStartDate = promoStartDate
    }

    Date getPromoEndDate() {
        return promoEndDate
    }

    void setPromoEndDate(Date promoEndDate) {
        this.promoEndDate = promoEndDate
    }

    String getPromoName() {
        return promoName
    }

    void setPromoName(String promoName) {
        this.promoName = promoName
    }

    BigDecimal getPrice2() {
        return price2
    }

    void setPrice2(BigDecimal price2) {
        this.price2 = price2
    }

    BigDecimal getCost() {
        return cost
    }

    void setCost(BigDecimal cost) {
        this.cost = cost
    }

    @Override
    String toString() {
        return name
    }

    String getQuality() {
        return quality
    }

    void setQuality(String quality) {
        this.quality = quality
    }

    String getCurrency() {
        return currency
    }

    void setCurrency(String currency) {
        this.currency = currency
    }

    Date getCreationDate() {
        return creationDate
    }

    void setCreationDate(Date creationDate) {
        this.creationDate = creationDate
    }
}
