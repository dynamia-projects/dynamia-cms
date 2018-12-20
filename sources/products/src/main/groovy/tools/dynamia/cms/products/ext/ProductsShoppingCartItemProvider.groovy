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
package tools.dynamia.cms.products.ext

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.shoppingcart.ShoppingCartItemProvider
import tools.dynamia.cms.shoppingcart.domain.ShoppingCartItem
import tools.dynamia.commons.BeanUtils

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class ProductsShoppingCartItemProvider implements ShoppingCartItemProvider {

	@Autowired
	private tools.dynamia.cms.products.services.ProductsService service

    @Override
    ShoppingCartItem getItem(Site site, String code) {
		try {

			tools.dynamia.cms.products.domain.Product product = service.getProductById(site, new Long(code))
            tools.dynamia.cms.products.domain.ProductsSiteConfig cfg = service.getSiteConfig(site)
            if (product == null) {
				return null
            }

			ShoppingCartItem item = createItem(product, cfg)

            List<tools.dynamia.cms.products.domain.RelatedProduct> related = service.getRelatedProducts(product, true)
            for (tools.dynamia.cms.products.domain.RelatedProduct relatedProduct : related) {
				ShoppingCartItem childItem = createItem(relatedProduct.product, cfg)
                childItem.unitPrice = relatedProduct.price
                if (relatedProduct.gift) {
                    childItem.unitPrice = BigDecimal.ZERO
                }
                childItem.editable = false
                childItem.parent = item
                item.children.add(childItem)
            }

			return item

        } catch (Exception e) {
			e.printStackTrace()
            return null
        }
	}

	private ShoppingCartItem createItem(tools.dynamia.cms.products.domain.Product product, tools.dynamia.cms.products.domain.ProductsSiteConfig cfg) {
		ShoppingCartItem item = new ShoppingCartItem()
        BeanUtils.setupBean(item, product)
        item.id = null
        item.code = product.id.toString()
        item.imageURL = "/resources/products/images/"
        item.imageName = product.image
        item.URL = "/store/products/" + product.id
        item.name = product.name
        item.sku = product.sku
        item.reference = product.reference
        item.unitPrice = tools.dynamia.cms.products.ProductsUtil.get().getUserPrice(product, cfg)
        item.refId = product.id
        item.refClass = tools.dynamia.cms.products.domain.Product.class.name
        item.description = product.description
        if (product.category != null) {
            item.categoryName = product.category.name
        }
		if (product.brand != null) {
            item.brandName = product.brand.name
        }
		if (product.promoEnabled && product.promoValue != null) {
            item.discount = product.realPromoValue
            item.discountName = product.promoName
        }

		return item
    }

}
