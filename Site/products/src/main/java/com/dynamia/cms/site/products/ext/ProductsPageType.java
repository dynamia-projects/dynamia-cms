/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.ext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.actions.SiteActionManager;
import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.pages.PageContext;
import com.dynamia.cms.site.pages.api.PageTypeExtension;
import com.dynamia.cms.site.pages.domain.PageParameter;
import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.products.services.ProductsService;

import tools.dynamia.domain.services.CrudService;

/**
 *
 * @author mario
 */
@CMSExtension
public class ProductsPageType implements PageTypeExtension {

    @Autowired
    private ProductsService service;

    @Autowired
    private CrudService crudService;

    @Override
    public String getId() {
        return "products";
    }

    @Override
    public String getName() {
        return "Products List Page";
    }

    @Override
    public String getDescription() {
        return "A product list page, you cant specified a category id using page parameters";
    }

    @Override
    public String getDescriptorId() {
        return "ProductGridConfig";
    }

    @Override
    public void setupPage(PageContext context) {
        ModelAndView mv = context.getModelAndView();
        mv.addObject("subtitle", context.getPage().getSubtitle());

        PageParameter categoryParam = context.getParameter("category");

        if (categoryParam != null) {
            Long id = new Long(categoryParam.getValue());
            SiteActionManager.performAction("loadProductCategory", mv, context.getRequest(), id);
            mv.setViewName("products/products");
        } else {
            ProductsUtil.setupProductsVar(service.getFeaturedProducts(context.getSite()), context.getModelAndView());
            mv.setViewName("products/main");
            SiteActionManager.performAction("showMainProductPage", mv, context.getRequest());

        }

    }

}
