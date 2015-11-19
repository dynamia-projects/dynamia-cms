/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.products.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.services.ProductsSynchronizer;

import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

/**
 *
 * @author mario
 */
@InstallAction
public class SyncAllAction extends AbstractCrudAction {

    @Autowired
    private ProductsSynchronizer synchronizer;

    public SyncAllAction() {
        setName("Sync All Products");
        setGroup(ActionGroup.get("products"));
        setImage("sync");
        setMenuSupported(true);
    }

    @Override
    public void actionPerformed(CrudActionEvent evt) {
        final ProductsSiteConfig cfg = (ProductsSiteConfig) evt.getData();
        if (cfg != null) {
            UIMessages.showQuestion("This action make take several minutes", () -> {
			    long s = System.currentTimeMillis();
			    synchronizer.synchronize(cfg);

			    long e = System.currentTimeMillis();
			    long t = e - s;

			    UIMessages.showMessage("Synchronization completed in " + t + "ms sucessfully!");
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
