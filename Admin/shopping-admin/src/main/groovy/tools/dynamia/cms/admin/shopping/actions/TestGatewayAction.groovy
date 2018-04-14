package tools.dynamia.cms.admin.shopping.actions

import org.springframework.beans.factory.annotation.Autowired
import org.zkoss.zul.Messagebox
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.payment.PaymentForm
import tools.dynamia.cms.site.payment.PaymentGateway
import tools.dynamia.cms.site.payment.PaymentUtils
import tools.dynamia.cms.site.payment.domain.PaymentGatewayConfig
import tools.dynamia.cms.site.payment.domain.PaymentTransaction
import tools.dynamia.cms.site.payment.services.PaymentService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState

@InstallAction
class TestGatewayAction extends AbstractCrudAction {

	@Autowired
	private PaymentService service

    TestGatewayAction() {
		setName("Test")
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
		PaymentGateway gateway = service.getDefaultGateway()
        PaymentTransaction tx = gateway.newTransaction("main", CMSUtil.getSiteURL(SiteContext.get().getCurrent(), "/"))
        tx.setCurrency("COP")
        tx.setEmail("user@emails.com")
        tx.setAmount(new BigDecimal(1000))
        tx.setTaxes(BigDecimal.ZERO)
        tx.setTaxesBase(BigDecimal.ZERO)
        tx.setTest(true)

        PaymentForm form = gateway.createForm(tx)

        StringBuilder sb = new StringBuilder()
        sb.append("TX: " + tx.getUuid() + "\n")
        sb.append("FORM: " + form.getUrl() + "  " + form.getHttpMethod() + "\n")
        sb.append(PaymentUtils.mapToString(form.getParameters()))

        Messagebox.show(sb.toString())

    }

}
