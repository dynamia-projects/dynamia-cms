/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.admin.core;

import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.Site;

import tools.dynamia.commons.BeanUtils;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.util.CrudServiceListenerAdapter;
import tools.dynamia.integration.sterotypes.Component;

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
