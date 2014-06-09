/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.actions;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.products.ProductCompareList;
import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.commons.StringUtils;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.integration.Containers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author mario_2
 */
@CMSAction
public class RemoveProductFromCompareList implements SiteAction {

    @Autowired
    private CrudService crudService;
    @Autowired
    private ProductsService service;

    @Override
    public String getName() {
        return "removeProductFromCompareList";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Long id = (Long) evt.getData();

        Product product = crudService.find(Product.class, id);
        ProductCompareList list = Containers.get().findObject(ProductCompareList.class);
        list.remove(product);

        ModelAndView mv = evt.getModelAndView();
        String action = "/store/compare/" + list.toString();
        ProductsUtil.setupDefaultVars(evt.getSite(), mv);
        if (list.getProducts().size() == 1) {
            CMSUtil.addSuccessMessage("Agregue otros productos de la misma categoria o similares para comparar", evt.getRedirectAttributes());
        } else if (list.getProducts().isEmpty()) {
            action = "/";
        }
        mv.setView(new RedirectView(action, true, true, false));

    }

}
