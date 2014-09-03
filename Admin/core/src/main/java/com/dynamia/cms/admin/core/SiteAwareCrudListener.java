package com.dynamia.cms.admin.core;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.commons.BeanUtils;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.util.CrudServiceListenerAdapter;
import com.dynamia.tools.integration.sterotypes.Component;

@Component
public class SiteAwareCrudListener extends CrudServiceListenerAdapter<SiteAware> {

	@Override
	public void beforeCreate(SiteAware entity) {
		configureSite(entity);
	}

	@Override
	public void beforeUpdate(SiteAware entity) {
		configureSite(entity);
	}

	private void configureSite(SiteAware entity) {
		try {
			if (entity.getSite() == null) {
				entity.setSite(SiteContext.get().getCurrent());
			}
		} catch (Exception e) {
			System.err.println("Error loading SiteHolder: " + e.getMessage());
		}
	}

	public void beforeQuery(QueryParameters params) {
		try {
			if (!params.containsKey("site")) {
				Class paramsType = params.getType();
				if (paramsType != null) {
					Object obj = BeanUtils.newInstance(paramsType);
					if (obj instanceof SiteAware) {
						Site site = SiteContext.get().getCurrent();
						if (site != null) {
							params.add("site", site);
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Error loading SiteHolder: " + e.getMessage());
		}
	}
}
