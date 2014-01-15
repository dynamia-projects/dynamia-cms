/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.products.actions;

import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.services.ProductsSyncService;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.AbstractCrudAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;
import com.dynamia.tools.web.util.Callback;
import com.dynamia.tools.web.util.ZKUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author mario
 */
@InstallAction
public class SyncAllAction extends AbstractCrudAction {

    @Autowired
    private ProductsSyncService syncService;

    public SyncAllAction() {
        setName("Sync All Products");
    }

    @Override
    public void actionPerformed(CrudActionEvent evt) {
        final ProductsSiteConfig cfg = (ProductsSiteConfig) evt.getData();
        if (cfg != null) {
            UIMessages.showQuestion("This action make take several minutes", new Callback() {

                @Override
                public void doSomething() {
                    long s = System.currentTimeMillis();
                    syncService.synchronizeAll(cfg);
                   
                    long e = System.currentTimeMillis();
                    long t = e-s;
                    
                     UIMessages.showMessage("Synchronization completed in "+t+"ms sucessfully!");
                }
            });

        } else {
            UIMessages.showMessage("Select product site configuration to sync.", MessageType.WARNING);
        }

    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }

    @Override
    public ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(ProductsSiteConfig.class);
    }

}
