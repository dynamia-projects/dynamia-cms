package tools.dynamia.cms.admin.shopping

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.core.services.SiteService
import tools.dynamia.cms.site.payment.services.PaymentService
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService

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
        List<Site> sites = siteService.getOnlineSites()

        for (Site site : sites) {

            ShoppingSiteConfig cfg = shoppingCartService.getConfiguration(site)
            if (cfg != null && cfg.isAutoSendPayments()) {
                String url = cfg.getPaymentsSenderURL()
                Map<String, String> params = cfg.getParametersAsMap()

                // send manual payments
                service.sendManualPayments(site.getKey(), url, params)

                // send auto payments
                service.sendPayments(site.getKey(), url, params)
            }

        }

    }
}
