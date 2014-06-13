/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.ext;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.cms.site.shoppingcart.ShoppingCartItemProvider;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingCartItem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author mario_2
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

            ShoppingCartItem item = new ShoppingCartItem();
            item.setCode(code);
            item.setImageURL("/resources/products/images/");
            item.setImageName(product.getImage());
            item.setURL("/store/products/" + product.getId());
            item.setName(product.getName());
            item.setUnitPrice(product.getPrice());
            item.setRefId(product.getId());
            item.setRefClass(Product.class.getName());
            item.setDescription(product.getDescription());

            return item;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
