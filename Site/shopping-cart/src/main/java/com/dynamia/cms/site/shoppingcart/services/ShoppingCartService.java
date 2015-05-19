/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.services;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingCart;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingCartItem;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig;

/**
 *
 * @author mario_2
 */
public interface ShoppingCartService {

    public ShoppingCartItem getItem(Site site, String code);

	public abstract ShoppingSiteConfig getConfiguration(Site site);

	public ShoppingOrder createOrder(ShoppingCart shoppingCart, ShoppingSiteConfig config);

	public abstract void cancelOrder(ShoppingOrder order);

	public abstract void saveOrder(ShoppingOrder order);

	public abstract void notifyOrderCompleted(ShoppingOrder order);

	public abstract void shipOrder(ShoppingOrder shoppingOrder);

	public abstract void notifyOrderShipped(ShoppingOrder order);

}
