/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
