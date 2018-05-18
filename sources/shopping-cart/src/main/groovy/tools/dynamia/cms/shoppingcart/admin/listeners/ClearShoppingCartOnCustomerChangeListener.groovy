package tools.dynamia.cms.shoppingcart.admin.listeners

import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.api.CMSListener
import tools.dynamia.cms.shoppingcart.ShoppingCartHolder
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.ext.CustomerChangeListener

@CMSListener
class ClearShoppingCartOnCustomerChangeListener implements CustomerChangeListener {

	@Override
    void onCustomerChange(User customer) {
		ModelAndView mv = new ModelAndView()
        mv.addObject("cartName", "shop")
		SiteActionManager.performAction("clearShoppingCart", mv, null)
        ShoppingCartHolder.get().clearAll()
    }

}
