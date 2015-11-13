/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.contraints.NotEmpty;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "prd_sites_config_parameters")
public class ProductsSiteConfigParameter extends SimpleEntity {

   
    @NotNull
    @ManyToOne
    private ProductsSiteConfig siteConfig;
    @NotEmpty(message = "Enter parameter value")
    @NotNull
    private String name;
    @Column(name = "paramValue")
    private String value;

    public ProductsSiteConfig getSiteConfig() {
        return siteConfig;
    }

    public void setSiteConfig(ProductsSiteConfig siteConfig) {
        this.siteConfig = siteConfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getName();
    }

}
