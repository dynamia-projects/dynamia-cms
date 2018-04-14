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
import tools.dynamia.cms.site.products.api.ValueType
import tools.dynamia.cms.site.products.dto.ProductDTO
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.*
import javax.validation.constraints.NotNull
import java.math.MathContext
import java.text.NumberFormat

/**
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_products")
class Product extends SimpleEntity implements SiteAware {

    @OneToOne
    @NotNull
    Site site

    @NotNull
    @NotEmpty(message = "Enter product name")
    String name
    @Column(length = 1000)
    String description
    @Lob
    @Basic(fetch = FetchType.LAZY)
    String longDescription
    @NotNull
    String sku
    String reference
    Long externalRef

    BigDecimal price
    BigDecimal price2
    BigDecimal cost
    BigDecimal lastPrice
    BigDecimal storePrice

    String priceDescription
    Long stock
    boolean active = true
    boolean featured
    boolean sale
    boolean newproduct

    String status
    String image
    String image2
    String image3
    String image4
    String videoURL
    String videoURL2

    Double stars = 0d
    Long stars1Count = 0L
    Long stars2Count = 0L
    Long stars3Count = 0L
    Long stars4Count = 0L
    Long stars5Count = 0L
    Long reviews = 0L
    Long views = 1L
    String currency

    @Column(length = 5000)
    String tags

    String externalLink

    String quality

    @OneToOne
    @NotNull(message = "Select product category")
    ProductCategory category

    @OneToOne
    ProductBrand brand

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @OrderBy("order")
    @JsonIgnore
    List<ProductDetail> details = new ArrayList<>()

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    List<ProductStock> stockDetails = new ArrayList<>()

    @OrderBy("number")
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    List<ProductCreditPrice> creditPrices = new ArrayList<>()

    boolean showLastPrice
    boolean showCreditPrices
    boolean sellInStore = true
    boolean sellInWeb = true

    // Promotion config
    boolean promoEnabled
    BigDecimal promoValue
    ValueType promoValueType = ValueType.PERCENT
    Date promoStartDate
    Date promoEndDate
    String promoName

    @OneToOne
    @JsonIgnore
    ProductTemplate template

    @OneToOne
    @JsonIgnore
    ProductTemplate alternateTemplate

    String taxName
    double taxPercent
    boolean taxable
    boolean taxIncluded
    String unit

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    List<ProductReview> userReviews = new ArrayList<>()


    String getPromoValueFormatted() {
        if (promoValue != null) {
            switch (promoValueType) {
                case CURRENCY:
                    return "-" + NumberFormat.getCurrencyInstance().format(promoValue)
                case PERCENT:
                    return "-" + promoValue.intValue() + "%"
            }
        }
        return ""
    }


    boolean hasStorePrice() {
        return storePrice != null && storePrice.longValue() > 0 && storePrice.compareTo(price) != 0
    }

    String getReference() {
        if (reference == null) {
            reference = ""
        }
        return reference
    }


    Long getViews() {
        if (views == null) {
            views = 1L
        }
        return views
    }


    Double getStars() {
        if (stars == null) {
            stars = 0.0
        }
        return stars
    }


    BigDecimal getRealPrice() {
        if (!isPromoEnabled()) {
            return getPrice()
        } else {
            return getPrice().subtract(getRealPromoValue())
        }
    }

    BigDecimal getRealLastPrice() {
        if (!isPromoEnabled()) {
            return lastPrice
        } else {
            return price
        }
    }


    @Override
    String toString() {
        return name
    }

    void sync(ProductDTO dto) {
        BeanUtils.setupBean(this, dto)

        active = dto.isActive()
        description = dto.getDescription()
        externalRef = dto.getExternalRef()
        featured = dto.isFeatured()
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            image = dto.getImage()
        }
        if (dto.getImage2() != null && !dto.getImage2().isEmpty()) {
            image2 = dto.getImage2()
        }
        if (dto.getImage3() != null && !dto.getImage3().isEmpty()) {
            image3 = dto.getImage3()
        }
        if (dto.getImage4() != null && !dto.getImage4().isEmpty()) {
            image4 = dto.getImage4()
        }
        lastPrice = dto.getLastPrice()
        longDescription = dto.getLongDescription()
        name = dto.getName()
        price = dto.getPrice()
        price2 = dto.getPrice2()
        cost = dto.getCost()
        sale = dto.isSale()
        sku = dto.getSku()
        stock = dto.getStock()
        tags = dto.getTags()
        status = dto.getStatus()
        externalLink = dto.getExternalLink()
        newproduct = dto.isNewproduct()
        showCreditPrices = dto.isShowCreditPrices()
        priceDescription = dto.getPriceDescription()
        reference = dto.getReference()
        sellInStore = dto.isSellInStore()
        sellInWeb = dto.isSellInWeb()
        showLastPrice = dto.isShowLastPrice()
        videoURL = dto.getVideoURL()
        videoURL2 = dto.getVideoURL2()
        storePrice = dto.getStorePrice()
        promoEnabled = dto.isPromoEnabled()
        promoEndDate = dto.getPromoEndDate()
        promoName = dto.getPromoName()
        promoStartDate = dto.getPromoStartDate()
        promoValue = dto.getPromoValue()
        promoValueType = dto.getPromoValueType()
        quality = dto.getQuality()
        currency = dto.getCurrency()
        taxable = dto.isTaxable()
        checkPromo()
    }

    void checkPromo() {
        if (promoStartDate != null && promoEndDate != null) {
            Date today = new Date()
            Calendar cal = Calendar.getInstance()
            cal.setTime(today)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            today = cal.getTime()

            promoEnabled = today.after(promoStartDate) && today.before(promoEndDate) || today == promoStartDate || today == promoEndDate
        }
    }

    BigDecimal getRealPromoValue() {
        BigDecimal realValue = promoValue
        if (getPromoValueType() == ValueType.PERCENT) {
            BigDecimal percent = realValue.divide(new BigDecimal(100), MathContext.DECIMAL32)
            realValue = getPrice() * percent
        }

        return realValue
    }

    ProductCategory getParentCategory() {
        if (category != null && category.getParent() != null) {
            return category.getParent()
        } else {
            return category
        }
    }

    ProductCategory getSubcategory() {
        if (category != null && category.getParent() != null) {
            return category
        } else {
            return null
        }
    }


    Long getReviews() {
        if (reviews == null) {
            reviews = 0L
        }
        return reviews
    }

}
