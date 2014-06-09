/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingCartItem;

/**
 *
 * @author mario_2
 */
public interface ShoppingCartItemProvider {

    public ShoppingCartItem getItem(Site site, String code);

}
