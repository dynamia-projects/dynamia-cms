/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.admin.ui.ui

import org.zkoss.util.Locales
import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.event.Events
import org.zkoss.zul.*
import tools.dynamia.actions.Action
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionEventBuilder
import tools.dynamia.cms.core.api.Module
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.cms.core.domain.ModuleInstanceParameter
import tools.dynamia.cms.core.services.impl.ModulesService
import tools.dynamia.commons.BeanMessages
import tools.dynamia.commons.MapBuilder
import tools.dynamia.domain.jpa.JpaParameter
import tools.dynamia.domain.query.Parameter
import tools.dynamia.integration.Containers
import tools.dynamia.viewers.Field
import tools.dynamia.viewers.ViewDescriptor
import tools.dynamia.viewers.ViewDescriptorNotFoundException
import tools.dynamia.viewers.util.Viewers
import tools.dynamia.zk.actions.ActionToolbar
import tools.dynamia.zk.crud.cfg.ConfigView
import tools.dynamia.zk.crud.cfg.ConfigViewRender
import tools.dynamia.zk.crud.ui.EntityPickerBox
import tools.dynamia.zk.util.ZKUtil
import tools.dynamia.zk.viewers.form.FormView
import tools.dynamia.zk.viewers.ui.Viewer

class ModuleInstanceUI extends Div implements ActionEventBuilder {

    private static final String CONFIG_ID_PREFIX = "module_"
    /**
     *
     */
    private static final long serialVersionUID = -3032514789042367569L
    private ActionToolbar toolbar
    private ModuleInstance moduleInstance
    private Component configurationUI
    private Borderlayout layout
    private FormView<ModuleInstance> formView
    private Listbox variables
    private BeanMessages messages = new BeanMessages(ModuleInstance, Locales.current)

    ModuleInstanceUI(ModuleInstance moduleInstance) {
        super()
        this.moduleInstance = moduleInstance
        initUI()
        initConfigurationUI()
    }

    private void initUI() {
        hflex = "1"
        vflex = "1"

        toolbar = new ActionToolbar(this)

        layout = new Borderlayout()
        layout.parent = this
        layout.appendChild(new North())
        layout.appendChild(new Center())
        layout.appendChild(new East())

        Vlayout top = new Vlayout()
        layout.north.appendChild(top)

        top.appendChild(toolbar)

        layout.center.title = messages.getMessage("configuration")
        layout.center.autoscroll = true

        formView = (FormView<ModuleInstance>) Viewers.getView(ModuleInstance.class, "form", moduleInstance)
        formView.style = "margin:0; padding:0;"
        TypeSelector typeSelector = (TypeSelector) formView.getFieldComponent("moduleId").inputComponent
        typeSelector.addEventListener(Events.ON_SELECT, { evt -> initConfigurationUI() })

        top.appendChild(formView)

        variables = new Listbox()
        variables.vflex = "1"
        variables.hflex = "1"
        variables.emptyMessage = "No variables"
        layout.east.title = messages.getMessage("variables")
        layout.east.width = "20%"
        layout.east.splittable = true
        layout.east.collapsible = true
        layout.east.appendChild(variables)

    }

    private void initConfigurationUI() {
        configurationUI = null
        layout.center.children.clear()

        ModulesService modulesService = Containers.get().findObject(ModulesService.class)
        Module module = modulesService.getModule(moduleInstance)
        if (module != null) {
            try {
                ViewDescriptor configDescriptor = Viewers.findViewDescriptor(CONFIG_ID_PREFIX + module.id)
                configureEntityConverter(configDescriptor)
                configurationUI = createCustomConfig(configDescriptor, moduleInstance, module)
            } catch (ViewDescriptorNotFoundException e) {
                configurationUI = new Label("No configuration found")
            }

            ZKUtil.fillListbox(variables, module.variablesNames, false)
        }

        if (configurationUI != null) {
            layout.center.appendChild(configurationUI)
        }

        if (formView != null && module != null) {
            Textbox customView = (Textbox) formView.getFieldComponent("customView").inputComponent
            customView.placeholder = module.templateViewName
        }

    }

    private void configureEntityConverter(ViewDescriptor configDescriptor) {
        for (Field field : (configDescriptor.fields)) {
            field.addParam(ConfigViewRender.PARAM_PARAMETER_NAME, field.name)

            if (field.componentClass == EntityPickerBox.class) {
                field.addParam(Viewers.PARAM_CONVERTER, converters.Entity.class.name)
            }
        }
    }

    @Override
    ActionEvent buildActionEvent(Object source, Map<String, Object> params) {
        ModulesService modulesService = Containers.get().findObject(ModulesService.class)
        Module module = modulesService.getModule(moduleInstance)

        Object data = formView.value
        params = MapBuilder.put("module", module, "moduleInstance", moduleInstance)
        if (configurationUI instanceof Viewer) {

            data = ((Viewer) configurationUI).value
        }

        return new ActionEvent(data, this, params)
    }

    private Viewer createCustomConfig(ViewDescriptor configDescriptor, final ModuleInstance moduleInstance,
                                      final Module module) {

        List<Parameter> configParameters = createConfigParameters(configDescriptor, module, moduleInstance)
        final Viewer viewer = new Viewer(configDescriptor, configParameters)
        viewer.style = "margin:0; padding: 0"
        ConfigView configView = (ConfigView) viewer.view
        configView.style = "margin:0; padding: 0"

        return viewer

    }

    private List<Parameter> createConfigParameters(ViewDescriptor configDescriptor, Module module,
                                                   ModuleInstance moduleInstance) {
        List<Parameter> cfgParameters = new ArrayList<>()
        if (moduleInstance.id != null) {
            moduleInstance.reloadParameters()
        }

        for (Field field : (configDescriptor.fields)) {
            ModuleInstanceParameter parameter = moduleInstance.getParameter(field.name)

            if (parameter == null) {
                parameter = new ModuleInstanceParameter(field.name,
                        (String) module.metadata.get(field.name))
            }

            String value = parameter.value
            if (parameter.extra != null && !parameter.extra.empty) {
                value = parameter.extra
            }
            try {
                if (value == null || value.empty && field.params.get("copyFrom") != null) {
                    ModuleInstanceParameter source = moduleInstance
                            .getParameter(field.params.get("copyFrom").toString())
                    if (source != null) {
                        value = source.value
                        if (source.extra != null && !source.extra.empty) {
                            value = source.extra
                        }
                    }
                }
            } catch (Exception e) {

            }
            cfgParameters.add(new JpaParameter(parameter.name, value))
        }
        return cfgParameters
    }

    void addAction(Action action) {
        toolbar.addAction(action)
    }

    void sync(ModuleInstance instance) {
        this.moduleInstance = instance
        formView.value = instance
        initConfigurationUI()
    }
}
