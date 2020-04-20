/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.pages.ext

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.api.CMSListener
import tools.dynamia.cms.pages.domain.Page
import tools.dynamia.domain.util.CrudServiceListenerAdapter

/**
 *
 * @author Mario Serrano Leones
 */
@CMSListener
class PageCrudListener extends CrudServiceListenerAdapter<Page> {

	@Autowired
	private tools.dynamia.cms.pages.services.PageService service

    @Override
    void beforeCreate(Page page) {
		if (page.id == null) {
            page.creationDate = new Date()
        }

		if (page.summary == null || page.summary.empty) {
			service.generateSummary(page)
        }

		if (page.imageURL == null || page.imageURL.empty) {
			service.generateImageURL(page)
        }

	}

	@Override
    void beforeUpdate(Page page) {
		beforeCreate(page)
        page.lastUpdate = new Date()

    }

}
