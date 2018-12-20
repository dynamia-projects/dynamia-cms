/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.core.domain

import org.hibernate.annotations.BatchSize
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.Orderable
import tools.dynamia.domain.contraints.NotEmpty
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "cr_modules_instances")
@BatchSize(size = 20)
class ModuleInstance extends Content implements Orderable {

    @NotEmpty(message = "Module ID is required")
    @NotNull
    private String moduleId
    @NotEmpty(message = "Select module position")
    private String position
    @Column(name = "moduleAlias")
    private String alias
    @NotEmpty(message = "Enter module title")
    private String title
    private boolean enabled = true
    private boolean titleVisible = true

    private String styleClass
    @OneToMany(mappedBy = "moduleInstance", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ModuleInstanceParameter> parameters = new ArrayList<>()

    @Column(name = "instanceOrder")
    private int order

    @Column(length = 3000)
    private String includePaths
    private String customView

    @Transient
    private Map<String, Object> model = new HashMap<String, Object>()

    @Transient
    private Object temporalForm

    String getIncludePaths() {
        return includePaths
    }

    void setIncludePaths(String includePaths) {
        this.includePaths = includePaths
    }

    String getCustomView() {
        return customView
    }

    void setCustomView(String customView) {
        this.customView = customView
    }

    @Override
    int getOrder() {
        return order
    }

    @Override
    void setOrder(int order) {
        this.order = order
    }

    String getTitle() {
        return title
    }

    void setTitle(String title) {
        this.title = title
    }

    String getModuleId() {
        return moduleId
    }

    void setModuleId(String moduleId) {
        this.moduleId = moduleId
    }

    String getPosition() {
        return position
    }

    void setPosition(String position) {
        this.position = position
    }

    String getAlias() {
        return alias
    }

    void setAlias(String alias) {
        this.alias = alias
    }

    boolean isEnabled() {
        return enabled
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled
    }

    String getStyleClass() {
        return styleClass
    }

    void setStyleClass(String styleClass) {
        this.styleClass = styleClass
    }

    List<ModuleInstanceParameter> getParameters() {
        return parameters
    }

    void setParameters(List<ModuleInstanceParameter> parameters) {
        this.parameters = parameters
    }

    boolean isTitleVisible() {
        return titleVisible
    }

    void setTitleVisible(boolean titleVisible) {
        this.titleVisible = titleVisible
    }

    @Override
    String toString() {
        return moduleId
    }

    String getParameterValue(String name) {
        ModuleInstanceParameter parameter = getParameter(name)
        if (parameter != null && parameter.enabled) {
            return parameter.value
        }

        return null
    }

    ModuleInstanceParameter getParameter(String name) {

        if (id != null && (parameters == null || parameters.empty)) {
            reloadParameters()
        }

        for (ModuleInstanceParameter parameter : parameters) {
            if (parameter.name.equalsIgnoreCase(name)) {
                return parameter
            }
        }
        return null
    }

    Map<String, Object> getModel() {
        return model
    }

    void addObject(String name, Object object) {
        model.put(name, object)
    }

    @Override
    ModuleInstance clone() {
        ModuleInstance clone = new ModuleInstance()
        clone.alias = ""
        clone.enabled = false
        clone.moduleId = moduleId
        clone.order = order + 1
        clone.position = position
        clone.styleClass = styleClass
        clone.title = title + "- copy"
        clone.titleVisible = titleVisible
        clone.customView = customView

        for (ModuleInstanceParameter parameter : (parameters)) {
            ModuleInstanceParameter cloneParam = parameter.clone()
            clone.parameters.add(cloneParam)
            cloneParam.moduleInstance = clone
        }

        return clone
    }

    boolean isPathIncluded(String currentPath) {
        if (includePaths == null || includePaths.empty) {
            return true
        }

        if (!includePaths.contains(",")) {
            return CMSUtil.matchAntPattern(includePaths, currentPath)
        } else {
            String[] paths = includePaths.split(",")
            for (String path : paths) {
                if (CMSUtil.matchAntPattern(path, currentPath)) {
                    return true
                }
            }
        }

        return false
    }

    void reloadParameters() {
        CrudService crudService = Containers.get().findObject(CrudService.class)
        parameters = crudService.find(ModuleInstanceParameter.class, "moduleInstance", this)
    }

    Object getTemporalForm() {
        return temporalForm
    }

    void setTemporalForm(Object temporalForm) {
        this.temporalForm = temporalForm
    }
}
