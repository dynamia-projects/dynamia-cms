/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.integration.Containers;
import java.io.Serializable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author mario_2
 */
@Component("siteHolder")
@Scope("session")
public class SiteHolder implements Serializable {

    private Site current;

    public static SiteHolder get() {
        return Containers.get().findObject(SiteHolder.class);
    }

    public Site getCurrent() {
        return current;
    }

    public void setCurrent(Site current) {
        this.current = current;
    }

}
