/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.services.impl;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.shoppingcart.ShoppingCartItemProvider;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingCartItem;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.integration.sterotypes.Service;

/**
 *
 * @author mario_2
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Override
    public ShoppingCartItem getItem(Site site, String code) {
        for (ShoppingCartItemProvider provider : Containers.get().findObjects(ShoppingCartItemProvider.class)) {
            ShoppingCartItem item = provider.getItem(site, code);
            if (item != null) {
                return item;
            }
        }
        return null;
    }

}
