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
package tools.dynamia.cms.products.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
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
    tools.dynamia.cms.products.api.ValueType promoValueType = tools.dynamia.cms.products.api.ValueType.PERCENT
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
                    return "-" + NumberFormat.currencyInstance.format(promoValue)
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
        if (!promoEnabled) {
            return price
        } else {
            return price.subtract(realPromoValue)
        }
    }

    BigDecimal getRealLastPrice() {
        if (!promoEnabled) {
            return lastPrice
        } else {
            return price
        }
    }


    @Override
    String toString() {
        return name
    }

    void sync(tools.dynamia.cms.products.dto.ProductDTO dto) {
        BeanUtils.setupBean(this, dto)

        active = dto.active
        description = dto.description
        externalRef = dto.externalRef
        featured = dto.featured
        if (dto.image != null && !dto.image.empty) {
            image = dto.image
        }
        if (dto.image2 != null && !dto.image2.empty) {
            image2 = dto.image2
        }
        if (dto.image3 != null && !dto.image3.empty) {
            image3 = dto.image3
        }
        if (dto.image4 != null && !dto.image4.empty) {
            image4 = dto.image4
        }
        lastPrice = dto.lastPrice
        longDescription = dto.longDescription
        name = dto.name
        price = dto.price
        price2 = dto.price2
        cost = dto.cost
        sale = dto.sale
        sku = dto.sku
        stock = dto.stock
        tags = dto.tags
        status = dto.status
        externalLink = dto.externalLink
        newproduct = dto.newproduct
        showCreditPrices = dto.showCreditPrices
        priceDescription = dto.priceDescription
        reference = dto.reference
        sellInStore = dto.sellInStore
        sellInWeb = dto.sellInWeb
        showLastPrice = dto.showLastPrice
        videoURL = dto.videoURL
        videoURL2 = dto.videoURL2
        storePrice = dto.storePrice
        promoEnabled = dto.promoEnabled
        promoEndDate = dto.promoEndDate
        promoName = dto.promoName
        promoStartDate = dto.promoStartDate
        promoValue = dto.promoValue
        promoValueType = dto.promoValueType
        quality = dto.quality
        currency = dto.currency
        taxable = dto.taxable
        checkPromo()
    }

    void checkPromo() {
        if (promoStartDate != null && promoEndDate != null) {
            Date today = new Date()
            Calendar cal = Calendar.instance
            cal.time = today
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            today = cal.time

            promoEnabled = today.after(promoStartDate) && today.before(promoEndDate) || today == promoStartDate || today == promoEndDate
        }
    }

    BigDecimal getRealPromoValue() {
        BigDecimal realValue = promoValue
        if (promoValueType == tools.dynamia.cms.products.api.ValueType.PERCENT) {
            BigDecimal percent = realValue.divide(new BigDecimal(100), MathContext.DECIMAL32)
            realValue = price * percent
        }

        return realValue
    }

    ProductCategory getParentCategory() {
        if (category != null && category.parent != null) {
            return category.parent
        } else {
            return category
        }
    }

    ProductCategory getSubcategory() {
        if (category != null && category.parent != null) {
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
