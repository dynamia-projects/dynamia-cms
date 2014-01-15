/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.ext;

import com.dynamia.cms.site.products.api.ProductsListener;

/**
 *
 * @author mario
 */
public class ProductListenerImpl implements ProductsListener {

    @Override
    public void productChanged(Long externalRef) {
        System.out.println("ACTUALIZAR PRODUCTO " + externalRef);

    }

    @Override
    public void categoryChanged(Long externalRef) {
        System.out.println("ACTUALIZAR CATEGORIA " + externalRef);

    }

}
