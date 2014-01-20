/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.actions;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.integration.Containers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@CMSAction
public class ShowProductAction implements SiteAction {

    @Autowired
    private ProductsService service;

    @Autowired
    private CrudService crudService;

    @Override
    public String getName() {
        return "showProduct";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.getModelAndView();
        Long id = (Long) evt.getData();

        Product product = crudService.find(Product.class, id);
        mv.addObject("prd_product", product);
        mv.addObject("prd_config", service.getSiteConfig(evt.getSite()));
        mv.addObject("title",product.getName().toUpperCase());
        mv.addObject("subtitle",product.getCategory().getName());
        mv.addObject("icon", "info-sign");
    }

}
