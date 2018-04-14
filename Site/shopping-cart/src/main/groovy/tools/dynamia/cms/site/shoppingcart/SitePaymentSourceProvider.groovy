package tools.dynamia.cms.site.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.payment.api.PaymentSource;
import tools.dynamia.cms.site.payment.api.PaymentSourceProvider;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig;
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import tools.dynamia.integration.sterotypes.Provider;

@Provider
public class SitePaymentSourceProvider implements PaymentSourceProvider {

    @Autowired
    private ShoppingCartService service;

    @Override
    public PaymentSource findSource(Object request) {
        Site site = SiteContext.get().getCurrent();

        if (site != null) {
            ShoppingSiteConfig confg = service.getConfiguration(site);
            String currency = confg != null ? confg.getDefaultCurrency() : null;
            return new PaymentSource(site.getKey(), CMSUtil.getSiteURL(site, "/"), site.getDescription(), currency);
        } else {
            return null;
        }
    }
}
