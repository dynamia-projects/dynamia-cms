/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.api;

/**
 * This interface is used by remote datasources to notify DynamiaCMS that some product or category as changed. ProductsListeners' implementation should be
 * running in the CMS side.
 *
 * @author mario
 */
public interface ProductsListener {

    public void productChanged(DataChangedEvent evt);

    public void categoryChanged(DataChangedEvent evt);

    public void brandChanged(DataChangedEvent evt);
    
    public void storeChanged(DataChangedEvent evt);
}
