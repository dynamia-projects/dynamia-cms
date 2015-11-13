/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.ext;

import java.util.Date;

import com.dynamia.cms.site.core.api.CMSListener;
import com.dynamia.cms.site.pages.domain.Page;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.domain.util.CrudServiceListenerAdapter;

/**
 *
 * @author mario
 */
@CMSListener
public class PageCrudListener extends CrudServiceListenerAdapter<Page> {

    @Override
    public void beforeCreate(Page entity) {
        if (entity.getId() == null) {
            entity.setCreationDate(new Date());
        }
        if (entity.getAlias() == null || entity.getAlias().isEmpty()) {
            entity.setAlias(entity.getTitle());
        }
        entity.setAlias(StringUtils.simplifiedString(entity.getAlias()));

    }

    @Override
    public void beforeUpdate(Page entity) {
        beforeCreate(entity);
    }

}
