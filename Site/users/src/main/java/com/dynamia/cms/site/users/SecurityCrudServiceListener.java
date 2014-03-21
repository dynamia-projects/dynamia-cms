package com.dynamia.cms.site.users;

import com.dynamia.tools.domain.BaseEntity;
import com.dynamia.tools.domain.util.CrudServiceListenerAdapter;
import org.springframework.stereotype.Component;

public class SecurityCrudServiceListener extends CrudServiceListenerAdapter<Object> {

    @Override
    public void beforeCreate(Object entity) {
        if (entity instanceof BaseEntity) {
            BaseEntity ent = (BaseEntity) entity;
            if (ent.getCreator() == null || ent.getCreator().isEmpty()) {
                ent.setCreator(UserHolder.get().getFullName());
            }
        }
    }

    @Override
    public void beforeUpdate(Object entity) {
        if (entity instanceof BaseEntity) {
            BaseEntity ent = (BaseEntity) entity;
            ent.setLastUpdater(UserHolder.get().getFullName());
        }
    }

    @Override
    public void beforeDelete(Object entity) {
        // TODO Auto-generated method stub
    }

}
