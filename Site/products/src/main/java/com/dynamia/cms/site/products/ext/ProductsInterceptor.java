/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.ext;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.api.SiteRequestInterceptorAdapter;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.shoppingcart.ShoppingCartHolder;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@CMSExtension
public class ProductsInterceptor extends SiteRequestInterceptorAdapter {

    @Override
    protected void afterRequest(Site site, ModelAndView mv) {

        if (mv.getViewName().contains("products") || mv.getModel().get("prd_products") != null || !ShoppingCartHolder.get().getCurrent().isEmpty()) {
            ProductsUtil.setupDefaultVars(site, mv);
        }

    }

}
