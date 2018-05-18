package tools.dynamia.cms.shoppingcart.actions.menus

import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.users.actions.UserMenuActionEnableable
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.domain.User

@CMSAction
class NewPaymentMenuAction implements UserMenuActionEnableable {

    @Override
    String getLabel() {
        return "Registrar Pago"
    }

    @Override
    String getDescription() {
        return "Registrar Pago Manual"
    }

    @Override
    String getIcon() {
        // TODO Auto-generated method stub
        return null
    }

    @Override
    int getOrder() {
        return 6
    }

    @Override
    String action() {
        return "shoppingcart/newpayment"
    }

    @Override
    boolean isEnabled(User currentUser) {

        return currentUser.site.corporateSite && currentUser.site.isParameter("manualPayments") && currentUser.profile == UserProfile.SELLER
    }

}
