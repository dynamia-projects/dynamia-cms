package tools.dynamia.cms.site.shoppingcart.actions.menus

import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.users.actions.UserMenuActionEnableable
import tools.dynamia.cms.site.users.api.UserProfile
import tools.dynamia.cms.site.users.domain.User

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

        return currentUser.getSite().isCorporateSite() && currentUser.getSite().isParameter("manualPayments") && currentUser.getProfile() == UserProfile.SELLER
    }

}
