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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.products.api.ValueType;
import tools.dynamia.cms.site.products.dto.ProductDTO;
import tools.dynamia.commons.BeanUtils;

import java.math.MathContext;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.contraints.NotEmpty;

/**
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_products")
public class Product extends SimpleEntity implements SiteAware {

	@OneToOne
	@NotNull
	private Site site;

	@NotNull
	@NotEmpty(message = "Enter product name")
	private String name;
	@Column(length = 1000)
	private String description;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String longDescription;
	@NotNull
	private String sku;
	private String reference;
	private Long externalRef;

	private BigDecimal price;
	private BigDecimal price2;
	private BigDecimal cost;
	private BigDecimal lastPrice;
	private BigDecimal storePrice;

	private String priceDescription;
	private Long stock;
	private boolean active = true;
	private boolean featured;
	private boolean sale;
	private boolean newproduct;

	private String status;
	private String image;
	private String image2;
	private String image3;
	private String image4;
	private String videoURL;
	private String videoURL2;

	private Double stars;
	private Long stars1Count;
	private Long stars2Count;
	private Long stars3Count;
	private Long stars4Count;
	private Long stars5Count;
	private Long reviews;
	private Long views = 1L;

	@Column(length = 5000)
	private String tags;

	private String externalLink;

	private String quality;

	@OneToOne
	@NotNull(message = "Select product category")
	private ProductCategory category;

	@OneToOne
	private ProductBrand brand;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@OrderBy("order")
	@JsonIgnore
	private List<ProductDetail> details = new ArrayList<>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ProductStock> stockDetails = new ArrayList<>();

	@OrderBy("number")
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ProductCreditPrice> creditPrices = new ArrayList<>();

	private boolean showLastPrice;
	private boolean showCreditPrices;
	private boolean sellInStore = true;
	private boolean sellInWeb = true;

	// Promotion config
	private boolean promoEnabled;
	private BigDecimal promoValue;
	private ValueType promoValueType = ValueType.PERCENT;
	private Date promoStartDate;
	private Date promoEndDate;
	private String promoName;

	@OneToOne
	@JsonIgnore
	private ProductTemplate template;

	@OneToOne
	@JsonIgnore
	private ProductTemplate alternateTemplate;

	private String taxName;
	private double taxPercent;
	private boolean taxable;
	private boolean taxIncluded;
	private String unit;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ProductReview> userReviews = new ArrayList<>();

	public List<ProductReview> getUserReviews() {
		return userReviews;
	}

	public void setUserReviews(List<ProductReview> userReviews) {
		this.userReviews = userReviews;
	}

	public Long getStars1Count() {
		return stars1Count;
	}

	public void setStars1Count(Long stars1Count) {
		this.stars1Count = stars1Count;
	}

	public Long getStars2Count() {
		return stars2Count;
	}

	public void setStars2Count(Long stars2Count) {
		this.stars2Count = stars2Count;
	}

	public Long getStars3Count() {
		return stars3Count;
	}

	public void setStars3Count(Long stars3Count) {
		this.stars3Count = stars3Count;
	}

	public Long getStars4Count() {
		return stars4Count;
	}

	public void setStars4Count(Long stars4Count) {
		this.stars4Count = stars4Count;
	}

	public Long getStars5Count() {
		return stars5Count;
	}

	public void setStars5Count(Long stars5Count) {
		this.stars5Count = stars5Count;
	}

	public boolean isTaxIncluded() {
		return taxIncluded;
	}

	public void setTaxIncluded(boolean taxIncluded) {
		this.taxIncluded = taxIncluded;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public double getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(double taxPercent) {
		this.taxPercent = taxPercent;
	}

	public boolean isTaxable() {
		return taxable;
	}

	public void setTaxable(boolean taxable) {
		this.taxable = taxable;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getVideoURL2() {
		return videoURL2;
	}

	public void setVideoURL2(String videoURL2) {
		this.videoURL2 = videoURL2;
	}

	public ProductTemplate getAlternateTemplate() {
		return alternateTemplate;
	}

	public void setAlternateTemplate(ProductTemplate alternateTemplate) {
		this.alternateTemplate = alternateTemplate;
	}

	public ProductTemplate getTemplate() {
		return template;
	}

	public void setTemplate(ProductTemplate template) {
		this.template = template;
	}

	public boolean isPromoEnabled() {
		return promoEnabled;
	}

	public void setPromoEnabled(boolean promoEnabled) {
		this.promoEnabled = promoEnabled;
	}

	public ValueType getPromoValueType() {
		return promoValueType;
	}

	public void setPromoValueType(ValueType promoValueType) {
		this.promoValueType = promoValueType;
	}

	public Date getPromoStartDate() {
		return promoStartDate;
	}

	public void setPromoStartDate(Date promoStartDate) {
		this.promoStartDate = promoStartDate;
	}

	public Date getPromoEndDate() {
		return promoEndDate;
	}

	public void setPromoEndDate(Date promoEndDate) {
		this.promoEndDate = promoEndDate;
	}

	public String getPromoName() {
		return promoName;
	}

	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}

	public String getPromoValueFormatted() {
		if (promoValue != null) {
			switch (promoValueType) {
			case CURRENCY:
				return "-" + NumberFormat.getCurrencyInstance().format(promoValue);
			case PERCENT:
				return "-" + promoValue.intValue() + "%";
			}
		}
		return "";
	}

	public BigDecimal getPromoValue() {
		return promoValue;
	}

	public void setPromoValue(BigDecimal promoValue) {
		this.promoValue = promoValue;
	}

	public boolean hasStorePrice() {
		return storePrice != null && storePrice.longValue() > 0 && storePrice.compareTo(price) != 0;
	}

	public BigDecimal getStorePrice() {
		return storePrice;
	}

	public void setStorePrice(BigDecimal storePrice) {
		this.storePrice = storePrice;
	}

	public String getVideoURL() {
		return videoURL;
	}

	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
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

	public String getPriceDescription() {
		return priceDescription;
	}

	public void setPriceDescription(String priceDescription) {
		this.priceDescription = priceDescription;
	}

	public List<ProductCreditPrice> getCreditPrices() {
		return creditPrices;
	}

	public void setCreditPrices(List<ProductCreditPrice> creditPrices) {
		this.creditPrices = creditPrices;
	}

	public boolean isShowCreditPrices() {
		return showCreditPrices;
	}

	public void setShowCreditPrices(boolean showCreditPrices) {
		this.showCreditPrices = showCreditPrices;
	}

	public boolean isShowLastPrice() {
		return showLastPrice;
	}

	public void setShowLastPrice(boolean showLastPrice) {
		this.showLastPrice = showLastPrice;
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

	public String getReference() {
		if (reference == null) {
			reference = "";
		}
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Long getViews() {
		if (views == null) {
			views = 1L;
		}
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getStars() {
		if (stars == null) {
			stars = 0.0;
		}
		return stars;
	}

	public void setStars(Double stars) {
		this.stars = stars;
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

	public BigDecimal getRealPrice() {
		if (!isPromoEnabled()) {
			return getPrice();
		} else {
			return getPrice().subtract(getRealPromoValue());
		}
	}

	public BigDecimal getRealLastPrice() {
		if (!isPromoEnabled()) {
			return lastPrice;
		} else {
			return price;
		}
	}

	public Long getStock() {
		return stock;
	}

	public void setStock(Long stock) {
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

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public ProductBrand getBrand() {
		return brand;
	}

	public void setBrand(ProductBrand brand) {
		this.brand = brand;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public List<ProductDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ProductDetail> details) {
		this.details = details;
	}

	public List<ProductStock> getStockDetails() {
		return stockDetails;
	}

	public void setStockDetails(List<ProductStock> stockDetails) {
		this.stockDetails = stockDetails;
	}

	public BigDecimal getPrice2() {
		return price2;
	}

	public void setPrice2(BigDecimal price2) {
		this.price2 = price2;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return name;
	}

	public void sync(ProductDTO dto) {
		BeanUtils.setupBean(this, dto);

		active = dto.isActive();
		description = dto.getDescription();
		externalRef = dto.getExternalRef();
		featured = dto.isFeatured();
		if (dto.getImage() != null && !dto.getImage().isEmpty()) {
			image = dto.getImage();
		}
		if (dto.getImage2() != null && !dto.getImage2().isEmpty()) {
			image2 = dto.getImage2();
		}
		if (dto.getImage3() != null && !dto.getImage3().isEmpty()) {
			image3 = dto.getImage3();
		}
		if (dto.getImage4() != null && !dto.getImage4().isEmpty()) {
			image4 = dto.getImage4();
		}
		lastPrice = dto.getLastPrice();
		longDescription = dto.getLongDescription();
		name = dto.getName();
		price = dto.getPrice();
		price2 = dto.getPrice2();
		cost = dto.getCost();
		sale = dto.isSale();
		sku = dto.getSku();
		stock = dto.getStock();
		tags = dto.getTags();
		status = dto.getStatus();
		externalLink = dto.getExternalLink();
		newproduct = dto.isNewproduct();
		showCreditPrices = dto.isShowCreditPrices();
		priceDescription = dto.getPriceDescription();
		reference = dto.getReference();
		sellInStore = dto.isSellInStore();
		sellInWeb = dto.isSellInWeb();
		showLastPrice = dto.isShowLastPrice();
		videoURL = dto.getVideoURL();
		videoURL2 = dto.getVideoURL2();
		storePrice = dto.getStorePrice();
		promoEnabled = dto.isPromoEnabled();
		promoEndDate = dto.getPromoEndDate();
		promoName = dto.getPromoName();
		promoStartDate = dto.getPromoStartDate();
		promoValue = dto.getPromoValue();
		promoValueType = dto.getPromoValueType();
		quality = dto.getQuality();

		checkPromo();
	}

	private void checkPromo() {
		if (promoStartDate != null && promoEndDate != null) {
			Date today = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			today = cal.getTime();

			promoEnabled = today.after(promoStartDate) && today.before(promoEndDate) || today.equals(promoStartDate)
					|| today.equals(promoEndDate);
		}
	}

	public BigDecimal getRealPromoValue() {
		BigDecimal realValue = promoValue;
		if (getPromoValueType() == ValueType.PERCENT) {
			BigDecimal percent = realValue.divide(new BigDecimal(100), MathContext.DECIMAL32);
			realValue = getPrice().multiply(percent);
		}

		return realValue;
	}

	public ProductCategory getParentCategory() {
		if (category != null && category.getParent() != null) {
			return category.getParent();
		} else {
			return category;
		}
	}

	public ProductCategory getSubcategory() {
		if (category != null && category.getParent() != null) {
			return category;
		} else {
			return null;
		}
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public Long getReviews() {
		if (reviews == null) {
			reviews = 0L;
		}
		return reviews;
	}

	public void setReviews(Long reviews) {
		this.reviews = reviews;
	}
}
