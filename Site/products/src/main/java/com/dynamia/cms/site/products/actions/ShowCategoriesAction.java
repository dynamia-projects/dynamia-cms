/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dynamia.cms.site.products.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.products.services.ProductsService;

/**
 *
 * @author mario
 */
@CMSAction
public class ShowCategoriesAction implements SiteAction{

    @Autowired
    private ProductsService service;
    
    @Override
    public String getName() {
        return "showProductCategories";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.getModelAndView();
        
        mv.addObject("title", "Categorias");
        mv.addObject("prd_categories", service.getCategories(evt.getSite()));
        
    }
    
}
