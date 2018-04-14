package tools.dynamia.cms.site.shoppingcart

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.payment.api.PaymentSource
import tools.dynamia.cms.site.payment.api.PaymentSourceProvider
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService
import tools.dynamia.integration.sterotypes.Provider

@Provider
class SitePaymentSourceProvider implements PaymentSourceProvider {

    @Autowired
    private ShoppingCartService service

    @Override
    PaymentSource findSource(Object request) {
        Site site = SiteContext.get().current

        if (site != null) {
            ShoppingSiteConfig confg = service.getConfiguration(site)
            String currency = confg != null ? confg.defaultCurrency : null
            return new PaymentSource(site.key, CMSUtil.getSiteURL(site, "/"), site.description, currency)
        } else {
            return null
        }
    }
}
