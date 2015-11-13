package com.dynamia.cms.admin.shopping.ui;

import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;

import com.dynamia.cms.site.payment.domain.PaymentTransaction;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingCartItem;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;

import tools.dynamia.viewers.util.Viewers;
import tools.dynamia.zk.viewers.form.FormView;
import tools.dynamia.zk.viewers.table.TableView;

public class ShoppingOrderDetailsUI extends Div {

	private ShoppingOrder shoppingOrder;

	public ShoppingOrderDetailsUI(ShoppingOrder order) {
		this.shoppingOrder = order;
		createUI();
	}

	private void createUI() {

		FormView<ShoppingOrder> orderFormView = (FormView<ShoppingOrder>) Viewers.getView(ShoppingOrder.class, "form", shoppingOrder);
		orderFormView.setReadOnly(true);

		FormView<PaymentTransaction> txFormView = (FormView<PaymentTransaction>) Viewers.getView(PaymentTransaction.class, "form",
				shoppingOrder.getTransaction());
		txFormView.setReadOnly(true);

		TableView<ShoppingCartItem> itemTableView = (TableView<ShoppingCartItem>) Viewers.getView(ShoppingCartItem.class, "table",
				shoppingOrder.getShoppingCart().getItems());
		itemTableView.setHeight("400px");
		itemTableView.setVflex(false);

		orderFormView.setParent(this);

		Groupbox title = new Groupbox();
		title.setMold("3d");
		title.setOpen(false);
		title.setClosable(false);
		title.appendChild(new Caption("Transaction Details"));
		title.setParent(this);

		txFormView.setParent(this);

		title = new Groupbox();
		title.setMold("3d");
		title.setOpen(false);
		title.setClosable(false);
		title.appendChild(new Caption("Order Details"));
		title.setParent(this);

		itemTableView.setParent(this);
	}

}
