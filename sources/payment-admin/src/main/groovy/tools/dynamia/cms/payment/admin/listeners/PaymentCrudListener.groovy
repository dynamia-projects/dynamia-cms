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

package tools.dynamia.cms.payment.admin.listeners

import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.payment.api.Payment
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.util.CrudServiceListenerAdapter
import tools.dynamia.integration.sterotypes.Listener

@Listener
class PaymentCrudListener extends CrudServiceListenerAdapter<Payment> {

	@Override
    void beforeCreate(Payment entity) {
		if (entity.source == null) {
			try {
                entity.source = SiteContext.get().current.key
            } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace()
            }
		}

	}

	@Override
    void beforeQuery(QueryParameters params) {
		Class entityClass = params.getType()

        if (BeanUtils.isAssignable(entityClass, Payment.class)) {

			if (!params.containsKey("source")) {
				String source = null
                try {
					source = SiteContext.get().current.key
                } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace()
                }

				if (source != null) {
					params.put("source", QueryConditions.eq(source))
                }
			}
		}
	}
}
