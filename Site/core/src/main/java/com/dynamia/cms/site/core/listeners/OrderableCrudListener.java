package com.dynamia.cms.site.core.listeners;

import com.dynamia.cms.site.core.Orderable;
import com.dynamia.tools.commons.BeanUtils;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.util.CrudServiceListenerAdapter;
import com.dynamia.tools.integration.sterotypes.Component;

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