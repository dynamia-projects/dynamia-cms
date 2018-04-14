package tools.dynamia.cms.admin.menus.ui

import java.util.ArrayList
import java.util.List
import java.util.Map

import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.event.Events
import org.zkoss.zul.Borderlayout
import org.zkoss.zul.Center
import org.zkoss.zul.Label
import org.zkoss.zul.North
import org.zkoss.zul.Vlayout
import org.zkoss.zul.Window

import tools.dynamia.cms.admin.core.zk.ui.TypeSelector
import tools.dynamia.cms.site.menus.api.MenuItemType
import tools.dynamia.cms.site.menus.domain.MenuItem
import tools.dynamia.cms.site.menus.domain.MenuItemParameter
import tools.dynamia.cms.site.menus.services.MenuService

import tools.dynamia.actions.Action
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionEventBuilder
import tools.dynamia.commons.MapBuilder
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
        getChildren().clear()
        setHflex("1")
        setVflex("1")

        // Menu Item

        layoutMenu = new Borderlayout()
        layoutMenu.setParent(this)
        layoutMenu.setVflex("1")

        North north = new North()
        north.setParent(layoutMenu)

        Center center = new Center()
        center.setParent(layoutMenu)
        center.setTitle("Configuration")
        center.setAutoscroll(true)

        Vlayout top = new Vlayout()
        top.setParent(north)


        toolbar = new ActionToolbar(this)
        toolbar.setParent(top)

        viewer = new Viewer("topMenuItem", menuItem)
        FormView formView = (FormView) viewer.getView()
        TypeSelector typeSelector = (TypeSelector) formView.getFieldComponent("type").getInputComponent()
        typeSelector.addEventListener(Events.ON_SELECT, { initConfigurationUI() })

        viewer.setParent(top)
    }

    private void initConfigurationUI() {
        configurationUI = null
        layoutMenu.getCenter().getChildren().clear()

        MenuService service = Containers.get().findObject(MenuService.class)
        MenuItemType menuItemType = service.getMenuItemType(menuItem)
        if (menuItemType != null) {
            try {
                ViewDescriptor configDescriptor = Viewers.findViewDescriptor(CONFIG_ID_PREFIX + menuItemType.getId())
                configureEntityConverter(configDescriptor)
                configurationUI = createCustomConfig(configDescriptor, menuItem, menuItemType)
            } catch (ViewDescriptorNotFoundException e) {
                configurationUI = new Label()
            }
        }

        if (configurationUI != null) {
            layoutMenu.getCenter().appendChild(configurationUI)
        }
    }

    private void configureEntityConverter(ViewDescriptor configDescriptor) {
        for (Field field : configDescriptor.getFields()) {
            field.addParam("parameterName", field.getName())

            if (field.getComponentClass() == EntityPickerBox.class) {
                field.addParam("converter", converters.Entity.class.getName())
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
            data = ((ConfigView) configurationUI).getValue()
        }

        return new ActionEvent(data, this, params)
    }

    private ConfigView createCustomConfig(ViewDescriptor configDescriptor,
                                          final MenuItem menuItem, final MenuItemType type) {

        List<Parameter> configParameters = createConfigParameters(configDescriptor, type, menuItem)
        final ConfigView view = (ConfigView) Viewers.getView(configDescriptor)
        view.setValue(configParameters)
        return view

    }

    private List<Parameter> createConfigParameters(ViewDescriptor configDescriptor, MenuItemType type, MenuItem menuItem) {
        List<Parameter> cfgParameters = new ArrayList<>()

        for (Field field : configDescriptor.getFields()) {
            MenuItemParameter parameter = menuItem.getParameter(field.getName())

            if (parameter == null) {
                parameter = new MenuItemParameter(field.getName(), "")
            }

            String value = parameter.getValue()
            if (parameter.getExtra() != null && !parameter.getExtra().isEmpty()) {
                value = parameter.getExtra()
            }
            cfgParameters.add(new Parameter(parameter.getName(), value))
        }
        return cfgParameters
    }

    void addAction(Action action) {
        toolbar.addAction(action)
    }

    void sync(MenuItem item) {
        this.menuItem = item
        viewer.setValue(null)
        viewer.setValue(item)
        initConfigurationUI()


    }

    MenuItem getMenuItem() {
        return menuItem
    }

}
