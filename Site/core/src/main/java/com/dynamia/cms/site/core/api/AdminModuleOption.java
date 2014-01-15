/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.api;

import java.io.Serializable;

/**
 *
 * @author mario
 */
public class AdminModuleOption implements Serializable {

    private String id;
    private String name;
    private Class coreClass;

    public AdminModuleOption() {

    }

    public AdminModuleOption(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public AdminModuleOption(String id, String name, Class coreClass) {
        this.id = id;
        this.name = name;
        this.coreClass = coreClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getCoreClass() {
        return coreClass;
    }

    public void setCoreClass(Class coreClass) {
        this.coreClass = coreClass;
    }

}
