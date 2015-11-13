/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.listeners;

import com.dynamia.cms.site.core.api.CMSListener;
import com.dynamia.cms.site.products.domain.ProductBrand;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.domain.util.CrudServiceListenerAdapter;

/**
 *
 * @author mario
 */
@CMSListener
public class BrandCrudServiceListener extends CrudServiceListenerAdapter<ProductBrand> {

    @Override
    public void beforeCreate(ProductBrand entity) {
        generateAlias(entity);
    }

    @Override
    public void beforeUpdate(ProductBrand entity) {
        generateAlias(entity);
    }

    private void generateAlias(ProductBrand entity) {
        String name = entity.getName();

        name = StringUtils.simplifiedString(name);
        entity.setAlias(name);
    }

}
