package tools.dynamia.cms.admin.shopping.controllers;

import tools.dynamia.cms.site.payment.api.PaymentTransactionStatus;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;

import tools.dynamia.domain.query.QueryConditions;
import tools.dynamia.zk.crud.CrudController;

public class ShoppingOrderController extends CrudController<ShoppingOrder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 113285947809724489L;

	@Override
	protected void beforeQuery() {
		if (!getParams().containsKey("transaction.status")) {
			setParemeter("transaction.status", QueryConditions.in(PaymentTransactionStatus.COMPLETED, PaymentTransactionStatus.PROCESSING));
		}

		if (!getParams().containsKey("shipped")) {
			setParemeter("shipped", false);
		}
	}

}
