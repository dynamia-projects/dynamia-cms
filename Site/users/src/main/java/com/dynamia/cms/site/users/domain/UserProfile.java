/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.domain;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.domain.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "usr_users_profiles")
public class UserProfile extends BaseEntity implements SiteAware {

    @OneToOne
    @NotNull
    private Site site;

    @ManyToOne
    @JoinColumn(updatable = false)
    private Profile profile;
    @ManyToOne
    @JoinColumn(updatable = false)
    private User user;

    public UserProfile() {
    }

    public UserProfile(Profile profile, User user) {
        this.profile = profile;
        this.user = user;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile perfil) {
        this.profile = perfil;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
