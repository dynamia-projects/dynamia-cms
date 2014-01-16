/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.domain;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.domain.SimpleEntity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "prd_sites_config")
public class ProductsSiteConfig extends SimpleEntity {

    @OneToOne
    @NotNull
    @JoinColumn(unique = true)
    private Site site;

    private String pricePattern;
    private String defaultCurrency;

    private String datasourceURL;
    private String datasourceUsername;
    private String datasourcePassword;
    private String datasourceImagesURL;
    private String datasourceBrandImagesURL;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastSync;

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public Date getLastSync() {
        return lastSync;
    }

    public void setLastSync(Date lastSync) {
        this.lastSync = lastSync;
    }

    public String getDatasourceURL() {
        return datasourceURL;
    }

    public void setDatasourceURL(String datasourceURL) {
        this.datasourceURL = datasourceURL;
    }

    public String getDatasourceUsername() {
        return datasourceUsername;
    }

    public void setDatasourceUsername(String datasourceUsername) {
        this.datasourceUsername = datasourceUsername;
    }

    public String getDatasourcePassword() {
        return datasourcePassword;
    }

    public void setDatasourcePassword(String datasourcePassword) {
        this.datasourcePassword = datasourcePassword;
    }

    public String getDatasourceImagesURL() {
        return datasourceImagesURL;
    }

    public void setDatasourceImagesURL(String datasourceImagesURL) {
        this.datasourceImagesURL = datasourceImagesURL;
    }

    public String getDatasourceBrandImagesURL() {
        return datasourceBrandImagesURL;
    }

    public void setDatasourceBrandImagesURL(String datasourceBrandImagesURL) {
        this.datasourceBrandImagesURL = datasourceBrandImagesURL;
    }

    public String getPricePattern() {
        return pricePattern;
    }

    public void setPricePattern(String pricePattern) {
        this.pricePattern = pricePattern;
    }

}
