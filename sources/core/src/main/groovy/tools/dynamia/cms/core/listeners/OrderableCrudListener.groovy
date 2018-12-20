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
package tools.dynamia.cms.core.listeners

import tools.dynamia.cms.core.Orderable
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.util.CrudServiceListenerAdapter
import tools.dynamia.integration.sterotypes.Component

/**
 * Created by Mario on 18/11/2014.
 */
@Component
class OrderableCrudListener extends CrudServiceListenerAdapter<Orderable> {

    @Override
    void beforeCreate(Orderable entity) {
        fixOrder(entity)
    }

    @Override
    void beforeUpdate(Orderable entity) {
        fixOrder(entity)
    }

    private void fixOrder(Orderable entity) {
        if (entity.order < 0) {
            entity.order = 0
        }
    }

    @Override
    void beforeQuery(QueryParameters params) {
        try {

            Object testInstance = BeanUtils.newInstance(params.getType())
            if(testInstance instanceof  Orderable) {
                if (params.getSorter() == null) {
                    params.orderBy("order", true)
                }
            }
        }catch (Exception e){

        }
    }
}
