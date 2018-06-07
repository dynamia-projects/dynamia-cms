package tools.dynamia.cms.menus.admin.ui

import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.event.Events
import org.zkoss.zul.*
import tools.dynamia.actions.Action
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionEventBuilder
import tools.dynamia.cms.admin.ui.ui.TypeSelector
import tools.dynamia.cms.menus.api.MenuItemType
import tools.dynamia.cms.menus.domain.MenuItem
import tools.dynamia.cms.menus.domain.MenuItemParameter
import tools.dynamia.cms.menus.services.MenuService
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
import tools.dynamia.zk.crud.ui.EntityPickerBox
import tools.dynamia.zk.viewers.form.FormView
import tools.dynamia.zk.viewers.ui.Viewer

class MenuItemsUI extends Window implements ActionEventBuilder {

    private static final String CONFIG_ID_PREFIX = "menuitem_"
    /**
     *
     */
    private static final long serialVersionUID = -3032514789042367569L
    private ActionToolbar toolbar

    private MenuItem menuItem
    private Component configurationUI
    private Borderlayout layoutMenu

    private Viewer viewer

    MenuItemsUI(MenuItem menuItem) {

        this.menuItem = menuItem
        initUI()
        initConfigurationUI()
    }

    private void initUI() {
        System.err.println("RENDERING MENUS ITEMS UI")
        children.clear()
        hflex = "1"
        vflex = "1"

        // Menu Item

        layoutMenu = new Borderlayout()
        layoutMenu.parent = this
        layoutMenu.vflex = "1"

        North north = new North()
        north.parent = layoutMenu

        Center center = new Center()
        center.parent = layoutMenu
        center.title = "Configuration"
        center.autoscroll = true

        Vlayout top = new Vlayout()
        top.parent = north


        toolbar = new ActionToolbar(this)
        toolbar.parent = top

        viewer = new Viewer("topMenuItem", menuItem)
        FormView formView = (FormView) viewer.view
        TypeSelector typeSelector = (TypeSelector) formView.getFieldComponent("type").inputComponent
        typeSelector.addEventListener(Events.ON_SELECT, { initConfigurationUI() })

        viewer.parent = top
    }

    private void initConfigurationUI() {
        configurationUI = null
        layoutMenu.center.children.clear()

        MenuService service = Containers.get().findObject(MenuService.class)
        MenuItemType menuItemType = service.getMenuItemType(menuItem)
        if (menuItemType != null) {
            try {
                ViewDescriptor configDescriptor = Viewers.findViewDescriptor(CONFIG_ID_PREFIX + menuItemType.id)
                configureEntityConverter(configDescriptor)
                configurationUI = createCustomConfig(configDescriptor, menuItem, menuItemType)
            } catch (ViewDescriptorNotFoundException e) {
                configurationUI = new Label()
            }
        }

        if (configurationUI != null) {
            layoutMenu.center.appendChild(configurationUI)
        }
    }

    private void configureEntityConverter(ViewDescriptor configDescriptor) {
        for (Field field : (configDescriptor.fields)) {
            field.addParam("parameterName", field.name)

            if (field.componentClass == EntityPickerBox.class) {
                field.addParam("converter", converters.Entity.class.name)
            }
        }
    }

    @Override
    ActionEvent buildActionEvent(Object source, Map<String, Object> params) {
        MenuService service = Containers.get().findObject(MenuService.class)
        MenuItemType menuItemType = service.getMenuItemType(menuItem)

        Object data = null
        params = MapBuilder.put("type", menuItemType, "menuItem", menuItem)
        if (configurationUI instanceof ConfigView) {
            data = ((ConfigView) configurationUI).value
        }

        return new ActionEvent(data, this, params)
    }

    private ConfigView createCustomConfig(ViewDescriptor configDescriptor,
                                          final MenuItem menuItem, final MenuItemType type) {

        List<Parameter> configParameters = createConfigParameters(configDescriptor, type, menuItem)
        final ConfigView view = (ConfigView) Viewers.getView(configDescriptor)
        view.value = configParameters
        return view

    }

    private List<Parameter> createConfigParameters(ViewDescriptor configDescriptor, MenuItemType type, MenuItem menuItem) {
        List<Parameter> cfgParameters = new ArrayList<>()

        for (Field field : (configDescriptor.fields)) {
            MenuItemParameter parameter = menuItem.getParameter(field.name)

            if (parameter == null) {
                parameter = new MenuItemParameter(field.name, "")
            }

            String value = parameter.value
            if (parameter.extra != null && !parameter.extra.empty) {
                value = parameter.extra
            }
            cfgParameters.add(new JpaParameter(parameter.name, value))
        }
        return cfgParameters
    }

    void addAction(Action action) {
        toolbar.addAction(action)
    }

    void sync(MenuItem item) {
        this.menuItem = item
        viewer.value = null
        viewer.value = item
        initConfigurationUI()


    }

    MenuItem getMenuItem() {
        return menuItem
    }

}
