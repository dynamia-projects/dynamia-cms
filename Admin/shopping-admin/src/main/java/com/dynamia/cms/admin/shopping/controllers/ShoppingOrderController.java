package com.dynamia.cms.admin.shopping.controllers;

import com.dynamia.cms.site.payment.domain.enums.PaymentTransactionStatus;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import com.dynamia.tools.web.crud.CrudController;

public class ShoppingOrderController extends CrudController<ShoppingOrder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 113285947809724489L;

	@Override
	protected void beforeQuery() {
		if (!getParams().containsKey("transaction.status")) {
			setParemeter("transaction.status", PaymentTransactionStatus.COMPLETED);
		}

		if (!getParams().containsKey("shipped")) {
			setParemeter("shipped", false);
		}
	}

}
