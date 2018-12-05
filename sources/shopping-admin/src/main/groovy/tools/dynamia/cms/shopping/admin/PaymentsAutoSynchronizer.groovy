/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
