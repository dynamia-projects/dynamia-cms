package com.dynamia.cms.admin.shopping.ui;

import java.util.Map;

import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.South;

import com.dynamia.cms.site.shoppingcart.domain.ShoppingCartItem;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;

import tools.dynamia.actions.Action;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.ActionEventBuilder;
import tools.dynamia.viewers.util.Viewers;
import tools.dynamia.zk.actions.ActionToolbar;
import tools.dynamia.zk.viewers.form.FormView;
import tools.dynamia.zk.viewers.table.TableView;

@SuppressWarnings("unchecked")
public class ShoppingOrderShipmentUI extends Borderlayout implements ActionEventBuilder {

	private ShoppingOrder shoppingOrder;
	private ActionToolbar toolbar = new ActionToolbar(this);

	public ShoppingOrderShipmentUI(ShoppingOrder order) {
		this.shoppingOrder = order;
		createUI();
	}

	private void createUI() {
		setHflex("1");
		setVflex("1");

		new Center().setParent(this);
		getCenter().setAutoscroll(true);
		Div content = new Div();
		content.setParent(getCenter());

		new South().setParent(this);
		toolbar.setParent(getSouth());

		FormView<ShoppingOrder> orderFormView = (FormView<ShoppingOrder>) Viewers.getView(Viewers
				.findViewDescriptor("ShoppingOrderShipment"));
		orderFormView.setReadOnly(true);

		TableView<ShoppingCartItem> itemTableView = (TableView<ShoppingCartItem>) Viewers.getView(ShoppingCartItem.class, "table",
				shoppingOrder.getShoppingCart().getItems());
		itemTableView.setHeight("400px");
		itemTableView.setVflex(false);

		orderFormView.setParent(content);

		Groupbox title = new Groupbox();
		title.setMold("3d");
		title.setOpen(false);
		title.setClosable(false);
		title.appendChild(new Caption("Order Details"));
		title.setParent(content);

		itemTableView.setParent(content);
	}

	@Override
	public ActionEvent buildActionEvent(Object source, Map<String, Object> params) {
		return new ActionEvent(shoppingOrder, this);
	}

	public void addAction(Action action) {
		toolbar.addAction(action);
	}

}
