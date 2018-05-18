/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.pages

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.SiteInitializer
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.domain.ContentAuthor
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.pages.domain.Page
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author mario
 */
@CMSExtension
class IndexPageSiteInitializer implements SiteInitializer {

    @Autowired
    private CrudService crudService

    @Override
    void init(Site site) {
    }

    @Override
    void postInit(Site site) {
        Page index = new Page()
        index.alias = "index"
        index.title = site.name
        index.site = site
        ContentAuthor author = getAuthor(site)
        index.author = author

        crudService.create(index)

    }

    private ContentAuthor getAuthor(Site site) {
        ContentAuthor author = crudService.findSingle(ContentAuthor.class, "site", site)
        if (author == null) {
            author = new ContentAuthor()
            author.firstName = "Default"
            author.lastName = "Author"
            author.email = "admin@" + site.key + ".login"
            crudService.save(author)
        }
        return author
    }

}
