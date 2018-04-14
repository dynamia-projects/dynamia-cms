package tools.dynamia.cms.site.products.modules

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.core.api.AbstractModule
import tools.dynamia.cms.site.core.api.CMSModule
import tools.dynamia.cms.site.core.api.ModuleContext
import tools.dynamia.cms.site.products.domain.Store
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.domain.services.CrudService

import java.util.List

@CMSModule
class StoresModule extends AbstractModule {

    @Autowired
    private ProductsService service

    @Autowired
    private CrudService crudService

    StoresModule() {
        super("stores", "Stores List", "products/modules/stores")
        setDescription("Show a stores list")
        putMetadata("author", "Mario Serrano Leones")
        putMetadata("version", "1.0")
        putMetadata("created at", "10-04-2017")
        setVariablesNames("stores")

    }

    @Override
    void init(ModuleContext context) {
        List<Store> stores = service.getStores(context.getSite())
        try {
            stores.forEach { s -> s.getContacts().size() }
        } catch (Exception e) {
        }


        context.getModuleInstance().addObject("stores", stores)

    }

}
