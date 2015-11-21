package com.dynamia.cms.site.core;

import java.nio.file.Path;

import com.dynamia.cms.site.core.domain.Site;

public class ModuleResourcesHandler extends SiteResourceHandler {

	@Override
	protected Path resolveResourceDirectory(Site site) {
		return DynamiaCMS.getModulesLocation();
	}

}
