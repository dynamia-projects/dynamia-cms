package com.dynamia.cms.admin.core.zk;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.tools.web.icons.AbstractIconsProvider;

@CMSExtension
public class CMSIconsProvider extends AbstractIconsProvider {

	@Override
	public String getExtension() {
		return "png";
	}

	@Override
	public String getPrefix() {
		return "cms/icons";
	}

}