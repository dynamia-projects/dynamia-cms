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
package tools.dynamia.cms.core

import tools.dynamia.cms.core.api.CMSListener
import tools.dynamia.domain.query.Parameter
import tools.dynamia.domain.util.CrudServiceListenerAdapter

/**
 *
 * @author Mario Serrano Leones
 */
@CMSListener
class ParamsCrudListener extends CrudServiceListenerAdapter<Parameter> {

	@Override
    void afterCreate(Parameter entity) {
		checkHomeVar(entity)
    }

	@Override
    void afterUpdate(Parameter entity) {
		checkHomeVar(entity)
    }

	private void checkHomeVar(Parameter entity) {
		if (DynamiaCMS.CFG_HOMEVAR.equals(entity.name)) {
			DynamiaCMS.reloadHomePath()
        }
	}

}
