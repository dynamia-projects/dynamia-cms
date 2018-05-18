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
