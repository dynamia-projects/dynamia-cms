/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.domain;

import com.dynamia.tools.domain.SimpleEntity;
import com.dynamia.tools.domain.contraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cr_modules_instances")
public class ModuleInstance extends SimpleEntity {

    @NotEmpty(message = "Module ID is required")
    @NotNull
    private String moduleId;
    @NotEmpty(message = "Select module position")
    private String position;
    @Column(name = "moduleAlias", unique = true)
    private String alias;
    private boolean enabled;
    private String styleClass;
    @OneToMany(mappedBy = "moduleInstance", cascade = CascadeType.ALL)
    private List<ModuleInstanceParameter> params = new ArrayList<>();

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public List<ModuleInstanceParameter> getParams() {
        return params;
    }

    public void setParams(List<ModuleInstanceParameter> params) {
        this.params = params;
    }

}
