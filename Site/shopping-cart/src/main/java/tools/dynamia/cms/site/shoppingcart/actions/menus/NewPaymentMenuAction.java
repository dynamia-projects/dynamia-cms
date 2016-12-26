package tools.dynamia.cms.site.shoppingcart.actions.menus;

import tools.dynamia.cms.site.core.api.CMSAction;
import tools.dynamia.cms.site.users.actions.UserMenuActionEnableable;
import tools.dynamia.cms.site.users.domain.User;

@CMSAction
public class NewPaymentMenuAction implements UserMenuActionEnableable {

	@Override
	public String getLabel() {
		return "Registrar Pago";
	}

	@Override
	public String getDescription() {
		return "Registrar Pago Manual";
	}

	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOrder() {
		return 6;
	}

	@Override
	public String action() {
		return "shoppingcart/newpayment";
	}

	@Override
	public boolean isEnabled(User currentUser) {

		return currentUser.getSite().isCorporateSite() && currentUser.getSite().isParameter("manualPayments");
	}

}
