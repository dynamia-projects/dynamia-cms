/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.domain;

import com.dynamia.tools.domain.SimpleEntity;
import com.dynamia.tools.domain.contraints.NotEmpty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cr_sites")
public class Site extends SimpleEntity {

    @NotEmpty
    private String name;
    @Column(name = "site_key", unique = true)
    @NotEmpty
    private String key;
    private String description;
    private boolean offline;
    private String offlineMessage;
    private String template = "dynamical";
    private String metadataKeywords;
    private String metadataAuthor;
    private String metadataDescription;
    private String metadataRights;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public String getOfflineMessage() {
        return offlineMessage;
    }

    public void setOfflineMessage(String offlineMessage) {
        this.offlineMessage = offlineMessage;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getMetadataKeywords() {
        return metadataKeywords;
    }

    public void setMetadataKeywords(String metadataKeywords) {
        this.metadataKeywords = metadataKeywords;
    }

    public String getMetadataAuthor() {
        return metadataAuthor;
    }

    public void setMetadataAuthor(String metadataAuthor) {
        this.metadataAuthor = metadataAuthor;
    }

    public String getMetadataDescription() {
        return metadataDescription;
    }

    public void setMetadataDescription(String metadataDescription) {
        this.metadataDescription = metadataDescription;
    }

    public String getMetadataRights() {
        return metadataRights;
    }

    public void setMetadataRights(String metadataRights) {
        this.metadataRights = metadataRights;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, key);
    }

}
