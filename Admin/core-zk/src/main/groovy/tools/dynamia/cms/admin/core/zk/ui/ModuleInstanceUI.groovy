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
package tools.dynamia.cms.admin.core.zk.ui

import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.event.Events
import org.zkoss.zul.*
import tools.dynamia.actions.Action
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionEventBuilder
import tools.dynamia.cms.site.core.api.Module
import tools.dynamia.cms.site.core.domain.ModuleInstance
import tools.dynamia.cms.site.core.domain.ModuleInstanceParameter
import tools.dynamia.cms.site.core.services.impl.ModulesService
import tools.dynamia.commons.MapBuilder
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

    ModuleInstanceUI(ModuleInstance moduleInstance) {
        super()
        this.moduleInstance = moduleInstance
        initUI()
        initConfigurationUI()
    }

    private void initUI() {
        setHflex("1")
        setVflex("1")

        toolbar = new ActionToolbar(this)

        layout = new Borderlayout()
        layout.setParent(this)
        layout.appendChild(new North())
        layout.appendChild(new Center())
        layout.appendChild(new East())

        Vlayout top = new Vlayout()
        layout.getNorth().appendChild(top)

        top.appendChild(toolbar)

        layout.getCenter().setTitle("Configuration")
        layout.getCenter().setAutoscroll(true)

        formView = (FormView<ModuleInstance>) Viewers.getView(ModuleInstance.class, "form", moduleInstance)
        formView.setStyle("margin:0; padding:0;")
        TypeSelector typeSelector = (TypeSelector) formView.getFieldComponent("moduleId").getInputComponent()
        typeSelector.addEventListener(Events.ON_SELECT, { evt -> initConfigurationUI() })

        top.appendChild(formView)

        variables = new Listbox()
        variables.setVflex("1")
        variables.setHflex("1")
        variables.setEmptyMessage("No variables")
        layout.getEast().setTitle("Module's View Variables")
        layout.getEast().setWidth("20%")
        layout.getEast().setSplittable(true)
        layout.getEast().setCollapsible(true)
        layout.getEast().appendChild(variables)

    }

    private void initConfigurationUI() {
        configurationUI = null
        layout.getCenter().getChildren().clear()

        ModulesService modulesService = Containers.get().findObject(ModulesService.class)
        Module module = modulesService.getModule(moduleInstance)
        if (module != null) {
            try {
                ViewDescriptor configDescriptor = Viewers.findViewDescriptor(CONFIG_ID_PREFIX + module.getId())
                configureEntityConverter(configDescriptor)
                configurationUI = createCustomConfig(configDescriptor, moduleInstance, module)
            } catch (ViewDescriptorNotFoundException e) {
                configurationUI = new Label("No configuration found")
            }

            ZKUtil.fillListbox(variables, module.getVariablesNames(), false)
        }

        if (configurationUI != null) {
            layout.getCenter().appendChild(configurationUI)
        }

        if (formView != null && module != null) {
            Textbox customView = (Textbox) formView.getFieldComponent("customView").getInputComponent()
            customView.setPlaceholder(module.getTemplateViewName())
        }

    }

    private void configureEntityConverter(ViewDescriptor configDescriptor) {
        for (Field field : configDescriptor.getFields()) {
            field.addParam(ConfigViewRender.PARAM_PARAMETER_NAME, field.getName())

            if (field.getComponentClass() == EntityPickerBox.class) {
                field.addParam(Viewers.PARAM_CONVERTER, converters.Entity.class.getName())
            }
        }
    }

    @Override
    ActionEvent buildActionEvent(Object source, Map<String, Object> params) {
        ModulesService modulesService = Containers.get().findObject(ModulesService.class)
        Module module = modulesService.getModule(moduleInstance)

        Object data = formView.getValue()
        params = MapBuilder.put("module", module, "moduleInstance", moduleInstance)
        if (configurationUI instanceof Viewer) {

            data = ((Viewer) configurationUI).getValue()
        }

        return new ActionEvent(data, this, params)
    }

    private Viewer createCustomConfig(ViewDescriptor configDescriptor, final ModuleInstance moduleInstance,
                                      final Module module) {

        List<Parameter> configParameters = createConfigParameters(configDescriptor, module, moduleInstance)
        final Viewer viewer = new Viewer(configDescriptor, configParameters)
        viewer.setStyle("margin:0; padding: 0")
        ConfigView configView = (ConfigView) viewer.getView()
        configView.setStyle("margin:0; padding: 0")

        return viewer

    }

    private List<Parameter> createConfigParameters(ViewDescriptor configDescriptor, Module module,
                                                   ModuleInstance moduleInstance) {
        List<Parameter> cfgParameters = new ArrayList<>()
        if (moduleInstance.getId() != null) {
            moduleInstance.reloadParameters()
        }

        for (Field field : configDescriptor.getFields()) {
            ModuleInstanceParameter parameter = moduleInstance.getParameter(field.getName())

            if (parameter == null) {
                parameter = new ModuleInstanceParameter(field.getName(),
                        (String) module.getMetadata().get(field.getName()))
            }

            String value = parameter.getValue()
            if (parameter.getExtra() != null && !parameter.getExtra().isEmpty()) {
                value = parameter.getExtra()
            }
            try {
                if (value == null || value.isEmpty() && field.getParams().get("copyFrom") != null) {
                    ModuleInstanceParameter source = moduleInstance
                            .getParameter(field.getParams().get("copyFrom").toString())
                    if (source != null) {
                        value = source.getValue()
                        if (source.getExtra() != null && !source.getExtra().isEmpty()) {
                            value = source.getExtra()
                        }
                    }
                }
            } catch (Exception e) {

            }
            cfgParameters.add(new Parameter(parameter.getName(), value))
        }
        return cfgParameters
    }

    void addAction(Action action) {
        toolbar.addAction(action)
    }

    void sync(ModuleInstance instance) {
        this.moduleInstance = instance
        formView.setValue(instance)
        initConfigurationUI()
    }
}
