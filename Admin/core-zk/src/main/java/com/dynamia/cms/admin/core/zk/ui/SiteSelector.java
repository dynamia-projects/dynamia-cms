/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.ui;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.viewers.zk.ComponentAliasIndex;
import com.dynamia.tools.web.util.ZKUtil;
import java.util.Collection;
import org.zkoss.zul.Combobox;

/**
 *
 * @author mario
 */
public class SiteSelector extends Combobox {

    static {
        ComponentAliasIndex.getInstance().add(SiteSelector.class);
    }

    public SiteSelector() {
        init();
    }

    public void init() {
        getChildren().clear();
        setReadonly(true);
        CrudService crudService = Containers.get().findObject(CrudService.class);
        Collection<Site> sites = crudService.findAll(Site.class, "name");
        ZKUtil.fillCombobox(this, sites, true);

    }

}
