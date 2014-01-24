/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.ext;

import com.dynamia.cms.site.core.actions.SiteActionManager;
import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.SearchForm;
import com.dynamia.cms.site.pages.api.SearchProvider;
import com.dynamia.cms.site.pages.api.SearchResult;
import com.dynamia.cms.site.products.ProductSearchForm;
import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.services.ProductsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@CMSExtension
public class ProductSearchProvider implements SearchProvider {

    @Override
    public SearchResult search(Site site, SearchForm form) {

        ModelAndView mv = new ModelAndView("products/productquery");
        ProductSearchForm pform = new ProductSearchForm();
        pform.setName(form.getQuery());

        SiteActionManager.performAction("searchProducts", mv, form.getRequest(), pform);

        SearchResult rs = new SearchResult(mv.getViewName(), false);
        rs.getEntries().putAll(mv.getModel());
        return rs;
    }

}
