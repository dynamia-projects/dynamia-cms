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
package tools.dynamia.cms.products.admin

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.services.ProductsSynchronizer
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@Service
class ProductsAutoSynchronizer {

    @Autowired
    private ProductsSynchronizer synchronizer
    @Autowired
    private CrudService crudService

    private LoggingService logger = new SLF4JLoggingService(ProductsAutoSynchronizer.class)

    //each 90 minutes
    @Scheduled(fixedDelay = 5400000L)
    void sync() {
        List<ProductsSiteConfig> configs = crudService.find(ProductsSiteConfig.class, "site.offline", false)
        for (ProductsSiteConfig config : configs) {
            try {
                synchronizer.synchronize(config)
            } catch (Throwable e) {
                logger.error("Error autosyncrhozing Products Site: $config.site", e)
                e.printStackTrace()
            }
        }

    }

}
