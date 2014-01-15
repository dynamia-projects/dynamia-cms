/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.api;

/**
 *
 * @author mario
 */
public class ConfigAdminModuleOption extends AdminModuleOption {

    private String descriptorID;

    public ConfigAdminModuleOption(String id, String name, String descriptorID) {
        super(id, name);
        this.descriptorID = descriptorID;

    }

    public String getDescriptorID() {
        return descriptorID;
    }

    public void setDescriptorID(String descriptorID) {
        this.descriptorID = descriptorID;
    }

}
