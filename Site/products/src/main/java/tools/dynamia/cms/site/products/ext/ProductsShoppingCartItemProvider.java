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
package tools.dynamia.cms.site.products.ext;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.site.core.api.CMSExtension;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.products.domain.Product;
import tools.dynamia.cms.site.products.domain.RelatedProduct;
import tools.dynamia.cms.site.products.services.ProductsService;
import tools.dynamia.cms.site.shoppingcart.ShoppingCartItemProvider;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingCartItem;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
public class ProductsShoppingCartItemProvider implements ShoppingCartItemProvider {

    @Autowired
    private ProductsService service;

    @Override
    public ShoppingCartItem getItem(Site site, String code) {
        try {

            Product product = service.getProductById(site, new Long(code));
            if (product == null) {
                return null;
            }

            ShoppingCartItem item = createItem(product);

            List<RelatedProduct> related = service.getRelatedProducts(product, true);
            for (RelatedProduct relatedProduct : related) {
                ShoppingCartItem childItem = createItem(relatedProduct.getProduct());
                childItem.setUnitPrice(relatedProduct.getPrice());
                if (relatedProduct.isGift()) {
                    childItem.setUnitPrice(BigDecimal.ZERO);                    
                }
                childItem.setEditable(false);
                childItem.setParent(item);
                item.getChildren().add(childItem);
            }

            return item;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ShoppingCartItem createItem(Product product) {
        ShoppingCartItem item = new ShoppingCartItem();
        item.setCode(product.getId().toString());
        item.setImageURL("/resources/products/images/");
        item.setImageName(product.getImage());
        item.setURL("/store/products/" + product.getId());
        item.setName(product.getName());
        item.setSku(product.getSku());
        item.setReference(product.getReference());
        item.setUnitPrice(product.getPrice());
        item.setRefId(product.getId());
        item.setRefClass(Product.class.getName());
        item.setDescription(product.getDescription());
        if (product.isPromoEnabled() && product.getPromoValue() != null) {
            item.setDiscount(product.getRealPromoValue());
            item.setDiscountName(product.getPromoName());
        }
        return item;
    }

}
