package tools.dynamia.cms.shopping.admin.controllers

import tools.dynamia.cms.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.zk.crud.CrudController

class ShoppingOrderController extends CrudController<ShoppingOrder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 113285947809724489L

    @Override
	protected void beforeQuery() {
		if (!params.containsKey("transaction.status")) {
			setParemeter("transaction.status", QueryConditions.in(PaymentTransactionStatus.COMPLETED, PaymentTransactionStatus.PROCESSING))
        }

		if (!params.containsKey("shipped")) {
			setParemeter("shipped", false)
        }
	}

}
