/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tools.dynamia.cms.admin

import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.util.CrudServiceListenerAdapter
import tools.dynamia.integration.sterotypes.Component

@Component
class SiteAwareCrudListener extends CrudServiceListenerAdapter<SiteAware> {

	@Override
    void beforeCreate(SiteAware entity) {
		configureSite(entity)
    }

	@Override
    void beforeUpdate(SiteAware entity) {
		configureSite(entity)
    }

	private void configureSite(SiteAware entity) {
		try {
			if (entity.site == null) {
                entity.site = SiteContext.get().current
            }
		} catch (Exception e) {
			System.err.println("Error loading SiteHolder: " + e.message)
        }
	}

    void beforeQuery(QueryParameters params) {
		try {
			if (!params.containsKey("site")) {
				Class paramsType = params.getType()
                if (paramsType != null) {
					Object obj = BeanUtils.newInstance(paramsType)
                    if (obj instanceof SiteAware) {
						Site site = SiteContext.get().current
                        if (site != null) {
							params.add("site", site)
                        }
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Error loading SiteHolder: " + e.message)
        }
	}
}
