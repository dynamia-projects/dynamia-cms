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

import com.dynamia.cms.admin.core.zk.EntityConverter;
import com.dynamia.cms.admin.core.zk.ui.TypeSelector;
import com.dynamia.cms.site.menus.api.MenuItemType;
import com.dynamia.cms.site.menus.domain.MenuItem;
import com.dynamia.cms.site.menus.domain.MenuItemParameter;
import com.dynamia.cms.site.menus.services.MenuService;
import com.dynamia.tools.commons.MapBuilder;
import com.dynamia.tools.domain.query.Parameter;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.viewers.Field;
import com.dynamia.tools.viewers.ViewDescriptor;
import com.dynamia.tools.viewers.ViewDescriptorNotFoundException;
import com.dynamia.tools.viewers.util.Viewers;
import com.dynamia.tools.viewers.zk.form.FormView;
import com.dynamia.tools.web.actions.Action;
import com.dynamia.tools.web.actions.ActionEvent;
import com.dynamia.tools.web.actions.ActionEventBuilder;
import com.dynamia.tools.web.cfg.ConfigView;
import com.dynamia.tools.web.ui.ActionToolbar;
import com.dynamia.tools.web.ui.EntityPickerBox;

public class MenuItemsUI extends Div implements ActionEventBuilder {

	private static final String CONFIG_ID_PREFIX = "menuitem_";
	/**
	 * 
	 */
	private static final long serialVersionUID = -3032514789042367569L;
	private ActionToolbar toolbar;
	private MenuItem menuItem;
	private Component configurationUI;
	private Borderlayout layout;
	private FormView<MenuItem> formView;

	public MenuItemsUI(MenuItem menuItem) {
		super();
		this.menuItem = menuItem;
		initUI();
		initConfigurationUI();
	}

	private void initUI() {
		setHflex("1");
		setVflex("1");

		toolbar = new ActionToolbar(this);

		layout = new Borderlayout();
		layout.setParent(this);
		layout.appendChild(new North());
		layout.appendChild(new South());
		layout.appendChild(new Center());

		layout.getSouth().appendChild(toolbar);
		layout.getCenter().setTitle("Configuration");
		layout.getCenter().setAutoscroll(true);

		formView = (FormView<MenuItem>) Viewers.getView(MenuItem.class, "form", menuItem);
		TypeSelector typeSelector = (TypeSelector) formView.getFieldComponent("type").getInputComponent();
		typeSelector.addEventListener(Events.ON_SELECT, evt -> initConfigurationUI());

		layout.getNorth().appendChild(formView);

	}

	private void initConfigurationUI() {
		configurationUI = null;
		layout.getCenter().getChildren().clear();

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
			layout.getCenter().appendChild(configurationUI);
		}
	}

	private void configureEntityConverter(ViewDescriptor configDescriptor) {
		for (Field field : configDescriptor.getFields()) {
			field.addParam("parameterName", field.getName());

			if (field.getComponentClass() == EntityPickerBox.class) {
				field.addParam("converter", EntityConverter.class.getName());
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
}
