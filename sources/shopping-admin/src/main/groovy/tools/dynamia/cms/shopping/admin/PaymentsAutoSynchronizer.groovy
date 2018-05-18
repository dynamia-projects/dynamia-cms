package tools.dynamia.cms.shopping.admin

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService

@Service
class PaymentsAutoSynchronizer {

    @Autowired
    private PaymentService service

    @Autowired
    private ShoppingCartService shoppingCartService

    @Autowired
    private SiteService siteService

    //each 10 minutes
    @Scheduled(fixedRate = 600000L)
    void sync() {
        List<Site> sites = siteService.onlineSites

        for (Site site : sites) {

            ShoppingSiteConfig cfg = shoppingCartService.getConfiguration(site)
            if (cfg != null && cfg.autoSendPayments) {
                String url = cfg.paymentsSenderURL
                Map<String, String> params = cfg.parametersAsMap

                // send manual payments
                service.sendManualPayments(site.key, url, params)

                // send auto payments
                service.sendPayments(site.key, url, params)
            }

        }

    }
}
