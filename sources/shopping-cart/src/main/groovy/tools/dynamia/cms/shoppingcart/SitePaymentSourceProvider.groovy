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
