/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
