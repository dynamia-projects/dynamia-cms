/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.domain;

import com.dynamia.tools.domain.SimpleEntity;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cr_modules_instances_params")
public class ModuleInstanceParameter extends SimpleEntity {

    private String paramName;
    private String paramValue;
    @ManyToOne
    private ModuleInstance moduleInstance;

    public ModuleInstanceParameter() {
    }

    public ModuleInstanceParameter(String paramName, String paramValue) {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    public ModuleInstance getModuleInstance() {
        return moduleInstance;
    }

    public void setModuleInstance(ModuleInstance moduleInstance) {
        this.moduleInstance = moduleInstance;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

}
