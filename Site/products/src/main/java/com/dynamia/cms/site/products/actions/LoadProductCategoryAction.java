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
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductBrand;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.domain.services.CrudService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@CMSAction
class LoadProductCategoryAction implements SiteAction {

    public static final String NAME = "loadProductCategory";

    @Autowired
    private ProductsService service;

    @Autowired
    private CrudService crudService;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.getModelAndView();

        ProductCategory category = null;
        if (evt.getData() instanceof String) {
            String alias = (String) evt.getData();
            category = service.getCategoryByAlias(evt.getSite(), alias);
        } else {
            Long id = (Long) evt.getData();
            category = crudService.find(ProductCategory.class, id);
        }

        List<Product> products = service.getProducts(category);
        List<ProductCategory> subcategories = service.getSubcategories(category);
        List<ProductBrand> categoryBrands = service.getBrands(category);

        mv.addObject("title", category.getName());
        mv.addObject("prd_category", category);
        mv.addObject("prd_subcategories", subcategories);
        mv.addObject("prd_categoryBrands", categoryBrands);
        mv.addObject("prd_parentCategory", category.getParent());

        products = ProductsUtil.setupPagination(products, evt.getRequest(), mv);
        ProductsUtil.setupProductsVar(products, mv);

    }

}
