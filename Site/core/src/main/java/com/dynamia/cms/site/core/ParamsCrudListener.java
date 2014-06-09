/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import com.dynamia.cms.site.core.api.CMSListener;
import com.dynamia.tools.domain.query.Parameter;
import com.dynamia.tools.domain.util.CrudServiceListenerAdapter;

/**
 *
 * @author mario_2
 */
@CMSListener
class ParamsCrudListener extends CrudServiceListenerAdapter<Parameter> {

    @Override
    public void afterCreate(Parameter entity) {
        checkHomeVar(entity);
    }

    @Override
    public void afterUpdate(Parameter entity) {
        checkHomeVar(entity);
    }

    private void checkHomeVar(Parameter entity) {
        if (DynamiaCMS.HOMEVAR.equals(entity.getName())) {
            DynamiaCMS.reloadHomePath();
        }
    }

}
