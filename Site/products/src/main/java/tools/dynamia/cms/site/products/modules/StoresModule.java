package tools.dynamia.cms.site.products.modules;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.site.core.api.AbstractModule;
import tools.dynamia.cms.site.core.api.CMSModule;
import tools.dynamia.cms.site.core.api.ModuleContext;
import tools.dynamia.cms.site.products.domain.Store;
import tools.dynamia.cms.site.products.services.ProductsService;
import tools.dynamia.domain.services.CrudService;

@CMSModule
public class StoresModule extends AbstractModule {

	@Autowired
	private ProductsService service;

	@Autowired
	private CrudService crudService;

	public StoresModule() {
		super("stores", "Stores List", "products/modules/stores");
		setDescription("Show a stores list");
		putMetadata("author", "Mario Serrano Leones");
		putMetadata("version", "1.0");
		putMetadata("created at", "10-04-2017");

	}

	@Override
	public void init(ModuleContext context) {
		List<Store> stores = service.getStores(context.getSite());
		stores.forEach(s -> s.getContacts().size());
		context.getModuleInstance().addObject("stores", stores);

	}

}
