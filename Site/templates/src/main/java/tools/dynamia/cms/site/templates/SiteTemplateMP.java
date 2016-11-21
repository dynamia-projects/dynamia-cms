package tools.dynamia.cms.site.templates;

import tools.dynamia.integration.sterotypes.Provider;
import tools.dynamia.navigation.Module;
import tools.dynamia.navigation.ModuleProvider;

@Provider
public class SiteTemplateMP implements ModuleProvider {

	@Override
	public Module getModule() {
		return new Module("cms", "DynamiaCMS");
	}

}
