/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.listeners;

import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.tools.commons.StringUtils;
import com.dynamia.tools.domain.util.CrudServiceListenerAdapter;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 *
 * @author mario
 */
@Component
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
