/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mario
 */
public class ProductDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1577777821132625315L;

	private String name;

	private String description;

	private String longDescription;

	private String sku;
	private String reference;
	private Long externalRef;

	private BigDecimal price;
	private BigDecimal lastPrice;
	private String priceDescription;
	private long stock;
	private boolean active;
	private boolean featured;
	private boolean sale;
	private boolean newproduct;
	private String status;
	private String image;
	private String image2;
	private String image3;
	private String image4;
	private String videoURL;

	private String tags;

	private ProductCategoryDTO category;

	private ProductBrandDTO brand;

	private List<ProductDetailDTO> details = new ArrayList<>();

	private List<ProductStockDTO> stockDetails = new ArrayList<>();

	private List<ProductCreditPriceDTO> creditPrices = new ArrayList<>();

	private String externalLink;

	private boolean showCreditPrices;
	private boolean showLastPrice;
	private boolean sellInStore = true;
	private boolean sellInWeb = true;

	public String getVideoURL() {
		return videoURL;
	}

	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}

	public boolean isShowLastPrice() {
		return showLastPrice;
	}

	public void setShowLastPrice(boolean showLastPrice) {
		this.showLastPrice = showLastPrice;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getPriceDescription() {
		return priceDescription;
	}

	public void setPriceDescription(String priceDescription) {
		this.priceDescription = priceDescription;
	}

	public boolean isShowCreditPrices() {
		return showCreditPrices;
	}

	public void setShowCreditPrices(boolean showCreditPrices) {
		this.showCreditPrices = showCreditPrices;
	}

	public List<ProductCreditPriceDTO> getCreditPrices() {
		return creditPrices;
	}

	public void setCreditPrices(List<ProductCreditPriceDTO> creditPrices) {
		this.creditPrices = creditPrices;
	}

	public boolean isNewproduct() {
		return newproduct;
	}

	public void setNewproduct(boolean newproduct) {
		this.newproduct = newproduct;
	}

	public String getExternalLink() {
		return externalLink;
	}

	public void setExternalLink(String externalLink) {
		this.externalLink = externalLink;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(Long externalRef) {
		this.externalRef = externalRef;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(BigDecimal lastPrice) {
		this.lastPrice = lastPrice;
	}

	public long getStock() {
		return stock;
	}

	public void setStock(long stock) {
		this.stock = stock;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isFeatured() {
		return featured;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

	public boolean isSale() {
		return sale;
	}

	public void setSale(boolean sale) {
		this.sale = sale;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getImage4() {
		return image4;
	}

	public void setImage4(String image4) {
		this.image4 = image4;
	}

	public ProductCategoryDTO getCategory() {
		return category;
	}

	public void setCategory(ProductCategoryDTO category) {
		this.category = category;
	}

	public ProductBrandDTO getBrand() {
		return brand;
	}

	public void setBrand(ProductBrandDTO brand) {
		this.brand = brand;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public List<ProductDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<ProductDetailDTO> details) {
		this.details = details;
	}

	public List<ProductStockDTO> getStockDetails() {
		return stockDetails;
	}

	public void setStockDetails(List<ProductStockDTO> stockDetails) {
		this.stockDetails = stockDetails;
	}

	public boolean isSellInStore() {
		return sellInStore;
	}

	public void setSellInStore(boolean sellInStore) {
		this.sellInStore = sellInStore;
	}

	public boolean isSellInWeb() {
		return sellInWeb;
	}

	public void setSellInWeb(boolean sellInWeb) {
		this.sellInWeb = sellInWeb;
	}

	@Override
	public String toString() {
		return name;
	}

}
