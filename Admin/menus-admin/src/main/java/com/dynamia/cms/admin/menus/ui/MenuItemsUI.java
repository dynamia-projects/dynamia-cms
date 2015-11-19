package com.dynamia.cms.admin.menus.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import com.dynamia.cms.admin.core.zk.ui.TypeSelector;
import com.dynamia.cms.admin.menus.actions.DeleteSubmenuAction;
import com.dynamia.cms.admin.menus.actions.EditSubmenuAction;
import com.dynamia.cms.admin.menus.actions.NewSubmenuAction;
import com.dynamia.cms.site.menus.api.MenuItemType;
import com.dynamia.cms.site.menus.domain.MenuItem;
import com.dynamia.cms.site.menus.domain.MenuItemParameter;
import com.dynamia.cms.site.menus.services.MenuService;

import tools.dynamia.actions.Action;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.ActionEventBuilder;
import tools.dynamia.commons.MapBuilder;
import tools.dynamia.domain.query.Parameter;
import tools.dynamia.integration.Containers;
import tools.dynamia.viewers.Field;
import tools.dynamia.viewers.ViewDescriptor;
import tools.dynamia.viewers.ViewDescriptorNotFoundException;
import tools.dynamia.viewers.util.Viewers;
import tools.dynamia.zk.actions.ActionToolbar;
import tools.dynamia.zk.crud.cfg.ConfigView;
import tools.dynamia.zk.crud.ui.EntityPickerBox;
import tools.dynamia.zk.viewers.form.FormView;
import tools.dynamia.zk.viewers.table.TableView;

public class MenuItemsUI extends Div implements ActionEventBuilder {

	private static final String CONFIG_ID_PREFIX = "menuitem_";
	/**
	 * 
	 */
	private static final long serialVersionUID = -3032514789042367569L;
	private ActionToolbar toolbar;
	private ActionToolbar toolbarSubmenus;
	private MenuItem menuItem;
	private Component configurationUI;
	private Borderlayout layoutMenu;
	private Borderlayout layoutSubmenus;
	private Tabbox tabbox;
	private FormView<MenuItem> formView;
	private TableView<MenuItem> subitemsTable;

	public MenuItemsUI(MenuItem menuItem) {
		super();
		this.menuItem = menuItem;
		initUI();
		initConfigurationUI();
		initDefaultActions();
	}

	private void initUI() {
		setHflex("1");
		setVflex("1");

		toolbar = new ActionToolbar(this);
		

		tabbox = new Tabbox();
		tabbox.setHflex("1");
		tabbox.setVflex("1");
		tabbox.appendChild(new Tabs());
		tabbox.appendChild(new Tabpanels());

		tabbox.getTabs().appendChild(new Tab("Menu Item"));
		tabbox.getTabs().appendChild(new Tab("Submenus"));

		Tabpanel panelMenu = new Tabpanel();
		panelMenu.setVflex("1");
		tabbox.getTabpanels().appendChild(panelMenu);

		Tabpanel panelSubmenus = new Tabpanel();
		panelSubmenus.setVflex("1");
		tabbox.getTabpanels().appendChild(panelSubmenus);
		appendChild(tabbox);

		// Menu Item

		layoutMenu = new Borderlayout();
		layoutMenu.appendChild(new North());
		layoutMenu.appendChild(new South());
		layoutMenu.appendChild(new Center());

		layoutMenu.getSouth().appendChild(toolbar);
		layoutMenu.getCenter().setTitle("Configuration");
		layoutMenu.getCenter().setAutoscroll(true);
		panelMenu.appendChild(layoutMenu);

		formView = (FormView<MenuItem>) Viewers.getView(Viewers.findViewDescriptor("topMenuItem"));
		formView.setValue(menuItem);
		TypeSelector typeSelector = (TypeSelector) formView.getFieldComponent("type").getInputComponent();
		typeSelector.addEventListener(Events.ON_SELECT, evt -> initConfigurationUI());

		layoutMenu.getNorth().appendChild(formView);

		// Submenus

		subitemsTable = (TableView<MenuItem>) Viewers.getView(MenuItem.class, "table", menuItem.getSubitems());

		toolbarSubmenus = new ActionToolbar((source, params) -> new ActionEvent(subitemsTable.getSelected(), this, params));

		layoutSubmenus = new Borderlayout();
		layoutSubmenus.appendChild(new North());
		layoutSubmenus.appendChild(new Center());

		layoutSubmenus.getNorth().appendChild(toolbarSubmenus);
		layoutSubmenus.getCenter().appendChild(subitemsTable);

		panelSubmenus.appendChild(layoutSubmenus);
	}

	private void initConfigurationUI() {
		configurationUI = null;
		layoutMenu.getCenter().getChildren().clear();

		MenuService service = Containers.get().findObject(MenuService.class);
		MenuItemType menuItemType = service.getMenuItemType(menuItem);
		if (menuItemType != null) {
			try {
				ViewDescriptor configDescriptor = Viewers.findViewDescriptor(CONFIG_ID_PREFIX + menuItemType.getId());
				configureEntityConverter(configDescriptor);
				configurationUI = createCustomConfig(configDescriptor, menuItem, menuItemType);
			} catch (ViewDescriptorNotFoundException e) {
				configurationUI = new Label();
			}
		}

		if (configurationUI != null) {
			layoutMenu.getCenter().appendChild(configurationUI);
		}
	}

	private void initDefaultActions() {
		addSubmenusAction(new NewSubmenuAction(menuItem));
		addSubmenusAction(new EditSubmenuAction(menuItem));
		addSubmenusAction(new DeleteSubmenuAction(menuItem));

	}

	private void configureEntityConverter(ViewDescriptor configDescriptor) {
		for (Field field : configDescriptor.getFields()) {
			field.addParam("parameterName", field.getName());

			if (field.getComponentClass() == EntityPickerBox.class) {
				field.addParam("converter", converters.Entity.class.getName());
			}
		}
	}

	@Override
	public ActionEvent buildActionEvent(Object source, Map<String, Object> params) {
		MenuService service = Containers.get().findObject(MenuService.class);
		MenuItemType menuItemType = service.getMenuItemType(menuItem);

		Object data = null;
		params = MapBuilder.put("type", menuItemType, "menuItem", menuItem);
		if (configurationUI instanceof ConfigView) {
			data = ((ConfigView) configurationUI).getValue();
		}

		return new ActionEvent(data, this, params);
	}

	private ConfigView createCustomConfig(ViewDescriptor configDescriptor, final MenuItem menuItem, final MenuItemType type) {

		List<Parameter> configParameters = createConfigParameters(configDescriptor, type, menuItem);
		final ConfigView view = (ConfigView) Viewers.getView(configDescriptor);
		view.setValue(configParameters);
		return view;

	}

	private List<Parameter> createConfigParameters(ViewDescriptor configDescriptor, MenuItemType type, MenuItem menuItem) {
		List<Parameter> cfgParameters = new ArrayList<>();

		for (Field field : configDescriptor.getFields()) {
			MenuItemParameter parameter = menuItem.getParameter(field.getName());

			if (parameter == null) {
				parameter = new MenuItemParameter(field.getName(), "");
			}

			String value = parameter.getValue();
			if (parameter.getExtra() != null && !parameter.getExtra().isEmpty()) {
				value = parameter.getExtra();
			}
			cfgParameters.add(new Parameter(parameter.getName(), value));
		}
		return cfgParameters;
	}

	public void addAction(Action action) {
		toolbar.addAction(action);
	}

	public void addSubmenusAction(Action action) {
		toolbarSubmenus.addAction(action);
	}

	public TableView<MenuItem> getSubitemsTable() {
		return subitemsTable;
	}

	public void reloadSubitems() {
		subitemsTable.setValue(menuItem.getSubitems());
	}
}
