/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.domain;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;

import tools.dynamia.domain.SimpleEntity;

/**
 *
 * @author mario
 */
@MappedSuperclass
public abstract class Content extends SimpleEntity implements SiteAware{

    @NotNull
    private String uuid;
   
    @OneToOne
    @NotNull
    private Site site;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

 
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
