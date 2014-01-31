/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.ext;

import com.dynamia.cms.site.core.actions.SiteActionManager;
import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.pages.PageContext;
import com.dynamia.cms.site.pages.api.PageTypeExtension;
import com.dynamia.cms.site.pages.domain.PageParameter;
import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.domain.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@CMSExtension
public class BrandsPageType implements PageTypeExtension {

    @Override
    public String getId() {
        return "products-brands";
    }

    @Override
    public String getName() {
        return "Product Brands List Page";
    }

    @Override
    public String getDescription() {
        return "A brand list page, you cant specified a brand id using page parameters";
    }

    @Override
    public String getDescriptorId() {
        return "BrandGridConfig";
    }

    @Override
    public void setupPage(PageContext context) {
        ModelAndView mv = context.getModelAndView();
        mv.addObject("subtitle", context.getPage().getSubtitle());

        SiteActionManager.performAction("showBrands", mv, context.getRequest());

    }

}
