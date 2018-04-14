/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.site.pages;

import org.springframework.beans.factory.annotation.Autowired;
import tools.dynamia.cms.site.core.SiteInitializer;
import tools.dynamia.cms.site.core.api.CMSExtension;
import tools.dynamia.cms.site.core.domain.ContentAuthor;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.pages.domain.Page;
import tools.dynamia.domain.services.CrudService;

/**
 *
 * @author mario
 */
@CMSExtension
public class IndexPageSiteInitializer implements SiteInitializer {

    @Autowired
    private CrudService crudService;

    @Override
    public void init(Site site) {
    }

    @Override
    public void postInit(Site site) {
        Page index = new Page();
        index.setAlias("index");
        index.setTitle(site.getName());
        index.setSite(site);
        ContentAuthor author = getAuthor(site);
        index.setAuthor(author);

        crudService.create(index);

    }

    private ContentAuthor getAuthor(Site site) {
        ContentAuthor author = crudService.findSingle(ContentAuthor.class, "site", site);
        if (author == null) {
            author = new ContentAuthor();
            author.setFirstName("Default");
            author.setLastName("Author");
            author.setEmail("admin@" + site.getKey() + ".login");
            crudService.save(author);
        }
        return author;
    }

}
