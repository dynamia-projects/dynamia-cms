/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.listeners;

import com.dynamia.cms.site.core.domain.Content;
import com.dynamia.tools.domain.util.CrudServiceListenerAdapter;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 *
 * @author mario
 */
@Component
public class ContentCrudListener extends CrudServiceListenerAdapter<Content> {

    @Override
    public void beforeCreate(Content entity) {

        entity.setUuid(UUID.randomUUID().toString().toUpperCase());

    }
}
