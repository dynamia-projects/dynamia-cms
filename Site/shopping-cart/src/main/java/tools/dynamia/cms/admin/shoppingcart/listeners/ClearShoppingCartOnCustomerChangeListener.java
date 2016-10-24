package tools.dynamia.cms.admin.shoppingcart.listeners;

import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.core.actions.SiteActionManager;
import tools.dynamia.cms.site.core.api.CMSListener;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.cms.site.users.ext.CustomerChangeListener;

@CMSListener
public class ClearShoppingCartOnCustomerChangeListener implements CustomerChangeListener {

	@Override
	public void onCustomerChange(User customer) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("cartName", "shop");		
		SiteActionManager.performAction("clearShoppingCart", mv, null);
	}

}
