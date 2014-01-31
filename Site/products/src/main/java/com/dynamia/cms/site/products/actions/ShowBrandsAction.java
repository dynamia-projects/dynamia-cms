/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.actions;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.products.services.ProductsService;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@CMSAction
public class ShowBrandsAction implements SiteAction {

    @Autowired
    private ProductsService service;

    @Override
    public String getName() {
        return "showBrands";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.getModelAndView();
        mv.addObject("title", "Nuestras marcas");
        mv.addObject("brandlist", service.getBrands(evt.getSite()));

        ProductsUtil.setupProductsVar(Collections.EMPTY_LIST, mv);
    }

}
