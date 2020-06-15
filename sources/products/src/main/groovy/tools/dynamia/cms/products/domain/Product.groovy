/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
import tools.dynamia.cms.products.api.ValueType
import tools.dynamia.cms.products.dto.ProductDTO
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
    boolean corporate
    boolean special

    String status
    String status2
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
    @Temporal(TemporalType.DATE)
    Date creationDate = new Date()

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
                case ValueType.CURRENCY:
                    return "-" + NumberFormat.currencyInstance.format(promoValue)
                case ValueType.PERCENT:
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

    void sync(ProductDTO dto) {
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
        status2 = dto.status2
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
        corporate = dto.corporate
        special = dto.special
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
