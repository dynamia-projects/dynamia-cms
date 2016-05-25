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
package tools.dynamia.cms.site.core.listeners;

import tools.dynamia.cms.site.core.Orderable;

import tools.dynamia.commons.BeanUtils;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.util.CrudServiceListenerAdapter;
import tools.dynamia.integration.sterotypes.Component;

/**
 * Created by Mario on 18/11/2014.
 */
@Component
public class OrderableCrudListener extends CrudServiceListenerAdapter<Orderable> {

    @Override
    public void beforeCreate(Orderable entity) {
        fixOrder(entity);
    }

    @Override
    public void beforeUpdate(Orderable entity) {
        fixOrder(entity);
    }

    private void fixOrder(Orderable entity) {
        if (entity.getOrder() < 0) {
            entity.setOrder(0);
        }
    }

    @Override
    public void beforeQuery(QueryParameters params) {
        try {

            Object testInstance = BeanUtils.newInstance(params.getType());
            if(testInstance instanceof  Orderable) {
                if (params.getSorter() == null) {
                    params.orderBy("order", true);
                }
            }
        }catch (Exception e){

        }
    }
}
