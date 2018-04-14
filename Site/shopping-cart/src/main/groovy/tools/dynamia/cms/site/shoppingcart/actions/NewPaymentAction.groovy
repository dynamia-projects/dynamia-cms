package tools.dynamia.cms.site.shoppingcart.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.core.html.Option
import tools.dynamia.cms.site.payment.PaymentHolder
import tools.dynamia.cms.site.payment.domain.ManualPayment
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.site.users.UserHolder
import tools.dynamia.cms.site.users.domain.User

@CMSAction
public class NewPaymentAction implements SiteAction {

	@Autowired
	private ShoppingCartService service;

	@Override
	public String getName() {
		return "createNewPayment";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {

		ModelAndView mv = evt.getModelAndView();
		mv.setViewName("payment/new");
		mv.addObject("title", "Nuevo Pago Manual");
		ShoppingSiteConfig config = service.getConfiguration(evt.getSite());

		ManualPayment pay = new ManualPayment();
		pay.setSource(evt.getSite().getKey());
		pay.setRegistrator(UserHolder.get().getFullName());
		pay.setRegistratorId(UserHolder.get().getCurrent().getId().toString());
		pay.setRegistratorCode(UserHolder.get().getCurrent().getCode());

		User customer = UserHolder.get().getCustomer();
		if (customer == null) {
			CMSUtil.addErrorMessage("Seleccione cliente para registrar pago", mv);
			return;
		}
		pay.setPayer(customer.getFullName());
		pay.setPayerCode(customer.getCode());
		pay.setPayerId(customer.getId().toString());

		loadPaymentTypes(mv, config);
		mv.addObject("payment", pay);

		PaymentHolder.get().setCurrentManualPayment(pay);

	}

	private void loadPaymentTypes(ModelAndView mv, ShoppingSiteConfig config) {
		String types = config.getPaymentTypes();
		if (types != null) {
			mv.addObject("paymentTypes", Option.buildFromArray(types.split(","), null));
		}
	}

}
