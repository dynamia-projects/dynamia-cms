/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import tools.dynamia.domain.SimpleEntity;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "usr_grants")
public class UserGrantedAuthority extends SimpleEntity {

    @ManyToOne
    private Profile profile;
    private String type;
    @Column(name = "grantvalue")
    private String value;
    private String description;

    public UserGrantedAuthority() {
    }

    public UserGrantedAuthority(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public UserGrantedAuthority(String type, String value, String description) {
        this.type = type;
        this.value = value;
        this.description = description;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
