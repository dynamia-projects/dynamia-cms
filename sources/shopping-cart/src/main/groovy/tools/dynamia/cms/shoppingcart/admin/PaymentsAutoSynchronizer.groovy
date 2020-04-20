/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package tools.dynamia.cms.shoppingcart.admin

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
