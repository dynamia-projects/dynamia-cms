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
package tools.dynamia.cms.products.dto

import tools.dynamia.cms.products.api.ValueType

/**
 * @author Mario Serrano Leones
 */
class ProductDTO implements Serializable {

    /**
     *
     */
    static final long serialVersionUID = 1577777821132625315L

    String name

    String description

    String longDescription

    String sku
    String reference
    Long externalRef

    BigDecimal price
    BigDecimal price2
    BigDecimal cost
    BigDecimal lastPrice
    BigDecimal storePrice
    String priceDescription
    long stock
    boolean active
    boolean featured
    boolean sale
    boolean newproduct
    String status
    String status2
    String image
    String image2
    String image3
    String image4
    String videoURL
    String videoURL2
    Date creationDate
    String tags
    ProductCategoryDTO category
    ProductBrandDTO brand

    List<ProductDetailDTO> details = new ArrayList<>()

    List<ProductStockDTO> stockDetails = new ArrayList<>()

    List<ProductCreditPriceDTO> creditPrices = new ArrayList<>()

    String externalLink

    boolean showCreditPrices
    boolean showLastPrice
    boolean sellInStore = true
    boolean sellInWeb = true

    // Promotion config
    boolean promoEnabled
    BigDecimal promoValue
    ValueType promoValueType = ValueType.PERCENT
    Date promoStartDate
    Date promoEndDate
    String promoName

    String taxName
    double taxPercent
    boolean taxable
    boolean taxIncluded
    String unit
    String quality
    String currency

    @Override
    String toString() {
        return name
    }

}
