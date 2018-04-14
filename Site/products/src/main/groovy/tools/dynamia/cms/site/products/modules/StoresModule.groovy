package tools.dynamia.cms.site.products.modules

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.core.api.AbstractModule
import tools.dynamia.cms.site.core.api.CMSModule
import tools.dynamia.cms.site.core.api.ModuleContext
import tools.dynamia.cms.site.products.domain.Store
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.domain.services.CrudService

@CMSModule
class StoresModule extends AbstractModule {

    @Autowired
    private ProductsService service

    @Autowired
    private CrudService crudService

    StoresModule() {
        super("stores", "Stores List", "products/modules/stores")
        description = "Show a stores list"
        putMetadata("author", "Mario Serrano Leones")
        putMetadata("version", "1.0")
        putMetadata("created at", "10-04-2017")
        variablesNames = "stores"

    }

    @Override
    void init(ModuleContext context) {
        List<Store> stores = service.getStores(context.site)
        try {
            stores.forEach { s -> s.contacts.size() }
        } catch (Exception e) {
        }


        context.moduleInstance.addObject("stores", stores)

    }

}
