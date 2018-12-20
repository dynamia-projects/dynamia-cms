/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
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
