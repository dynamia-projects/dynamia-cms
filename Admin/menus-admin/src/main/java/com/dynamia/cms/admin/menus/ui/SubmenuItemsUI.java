package com.dynamia.cms.admin.menus.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.North;

import com.dynamia.cms.site.menus.api.MenuItemType;
import com.dynamia.cms.site.menus.domain.MenuItem;
import com.dynamia.cms.site.menus.domain.MenuItemParameter;

import tools.dynamia.actions.Action;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.ActionEventBuilder;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.domain.query.Parameter;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.Containers;
import tools.dynamia.viewers.Field;
import tools.dynamia.viewers.ViewDescriptor;
import tools.dynamia.viewers.util.Viewers;
import tools.dynamia.zk.actions.ActionToolbar;
import tools.dynamia.zk.crud.CrudController;
import tools.dynamia.zk.crud.cfg.ConfigView;
import tools.dynamia.zk.crud.ui.EntityPickerBox;
import tools.dynamia.zk.viewers.table.TableView;

public class SubmenuItemsUI extends Div implements ActionEventBuilder {

	private static final long serialVersionUID = -3032514789042367569L;

	private ActionToolbar toolbarSubmenus = new ActionToolbar(this);
	private MenuItem menuItem;

	private Borderlayout layoutSubmenus;

	private TableView<MenuItem> subitemsTable;

	private CrudActionEvent sourceEvent;

	private InternalController controller = new InternalController();

	public SubmenuItemsUI(MenuItem menuItem) {
		super();
		this.menuItem = menuItem;

	}

	public SubmenuItemsUI(MenuItem menuItem, CrudActionEvent evt) {
		super();
		this.menuItem = menuItem;
		this.sourceEvent = evt;
	}

	private void initUI() {
		getChildren().clear();
		setHflex("1");
		setVflex("1");

		// Submenus

		subitemsTable = (TableView<MenuItem>) Viewers.getView(MenuItem.class, "table", Collections.EMPTY_LIST);

		layoutSubmenus = new Borderlayout();
		appendChild(layoutSubmenus);

		layoutSubmenus.setVflex("1");
		layoutSubmenus.appendChild(new North());
		layoutSubmenus.appendChild(new Center());

		layoutSubmenus.getNorth().appendChild(toolbarSubmenus);
		layoutSubmenus.getCenter().appendChild(subitemsTable);

		reloadSubitems();

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
		ActionEvent event = new ActionEvent(subitemsTable.getSelected(), this, params);

		if (sourceEvent != null) {
			event.getParams().put("crudview", sourceEvent.getView());
			event.getParams().put("controller", controller);
			event.getParams().put("sourceEvent", sourceEvent);
		}

		return event;
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
		toolbarSubmenus.addAction(action);
	}

	public TableView<MenuItem> getSubitemsTable() {
		return subitemsTable;
	}

	public void reloadSubitems() {
		CrudService crudService = Containers.get().findObject(CrudService.class);
		subitemsTable.setValue(crudService.find(MenuItem.class, QueryParameters.with("parentItem", menuItem).orderBy("order", true)));
	}

	@Override
	public void setParent(Component parent) {
		// TODO Auto-generated method stub
		super.setParent(parent);
		initUI();
	}

	class InternalController extends CrudController<MenuItem> {
		public InternalController() {
			
		}

		@Override
		public void query() {
			reloadSubitems();
			dataSetView = subitemsTable;
		}
	}
}
