/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk;

import com.dynamia.cms.site.core.domain.ContentAuthor;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.domain.PageCategory;
import org.springframework.stereotype.Component;

import com.dynamia.tools.web.crud.CrudPage;
import com.dynamia.tools.web.navigation.Module;
import com.dynamia.tools.web.navigation.ModuleProvider;
import com.dynamia.tools.web.navigation.PageGroup;

@Component("CoreModule")
public class Installer implements ModuleProvider {

    @Override
    public Module getModule() {
        Module module = new Module("admin", "Administration");
        module.setPosition(10);
        module.setIcon("icons:module");
        {
            PageGroup pg = new PageGroup("sites", "Sites");
            module.addPageGroup(pg);
            {
                pg.addPage(new CrudPage("sites", "Sites", Site.class));

            }
            pg = new PageGroup("content", "Content");
            module.addPageGroup(pg);
            {
                pg.addPage(new CrudPage("authors", "Authors", ContentAuthor.class));
                pg.addPage(new CrudPage("categories", "Pages Categories", PageCategory.class));
                pg.addPage(new CrudPage("pages", "Pages Content", com.dynamia.cms.site.pages.domain.Page.class));
            }

            pg = new PageGroup("modules", "Modules");
            module.addPageGroup(pg);
            {
                //pg.addPage(new CrudPage("menus", "Menus", Menu.class));
            }

        }

        return module;
    }
}
