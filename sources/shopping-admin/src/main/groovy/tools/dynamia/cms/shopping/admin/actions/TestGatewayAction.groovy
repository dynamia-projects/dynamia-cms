/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package tools.dynamia.cms.shopping.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import org.zkoss.zul.Messagebox
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.payment.PaymentForm
import tools.dynamia.cms.payment.PaymentGateway
import tools.dynamia.cms.payment.PaymentUtils
import tools.dynamia.cms.payment.domain.PaymentGatewayConfig
import tools.dynamia.cms.payment.domain.PaymentTransaction
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState

@InstallAction
class TestGatewayAction extends AbstractCrudAction {

	@Autowired
	private PaymentService service

    TestGatewayAction() {
        name = "Test"
    }

	@Override
    CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ)
    }

	@Override
    ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(PaymentGatewayConfig.class)
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		PaymentGateway gateway = service.defaultGateway
        PaymentTransaction tx = gateway.newTransaction("main", CMSUtil.getSiteURL(SiteContext.get().current, "/"))
        tx.currency = "COP"
        tx.email = "user@emails.com"
        tx.amount = new BigDecimal(1000)
        tx.taxes = BigDecimal.ZERO
        tx.taxesBase = BigDecimal.ZERO
        tx.test = true

        PaymentForm form = gateway.createForm(tx)

        StringBuilder sb = new StringBuilder()
        sb.append("TX: " + tx.uuid + "\n")
        sb.append("FORM: " + form.url + "  " + form.httpMethod + "\n")
        sb.append(PaymentUtils.mapToString(form.parameters))

        Messagebox.show(sb.toString())

    }

}
