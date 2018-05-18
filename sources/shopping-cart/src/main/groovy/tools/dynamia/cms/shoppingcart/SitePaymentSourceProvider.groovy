package tools.dynamia.cms.shoppingcart

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.payment.api.PaymentSource
import tools.dynamia.cms.payment.api.PaymentSourceProvider
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.integration.sterotypes.Provider

@Provider
class SitePaymentSourceProvider implements PaymentSourceProvider {

    @Autowired
    private ShoppingCartService service

    @Override
    PaymentSource findSource(Object request) {
        Site site = SiteContext.get().current

        if (site != null) {
            tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig confg = service.getConfiguration(site)
            String currency = confg != null ? confg.defaultCurrency : null
            return new PaymentSource(site.key, CMSUtil.getSiteURL(site, "/"), site.description, currency)
        } else {
            return null
        }
    }
}
