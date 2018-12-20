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
package tools.dynamia.cms.products.listeners

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.products.api.DataChangedEvent
import tools.dynamia.cms.products.api.InvalidTokenException
import tools.dynamia.cms.products.api.ProductsDatasource
import tools.dynamia.cms.products.api.ProductsListener
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.dto.ProductBrandDTO
import tools.dynamia.cms.products.dto.ProductCategoryDTO
import tools.dynamia.cms.products.dto.ProductDTO
import tools.dynamia.cms.products.dto.StoreDTO
import tools.dynamia.cms.products.services.ProductsService
import tools.dynamia.cms.products.services.ProductsSyncService
import tools.dynamia.cms.products.services.ProductsSynchronizer

/**
 *
 * @author Mario Serrano Leones
 */
class ProductListenerImpl implements ProductsListener {

    @Autowired
    private ProductsSyncService syncService

    @Autowired
    private ProductsService service

    @Autowired
    private ProductsSynchronizer synchronizer

    @Override
    void productChanged(final DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt)

        def ds = syncService.getDatasource(config)
        def dto = ds.getProduct(evt.externalRef, config.parametersAsMap)
        synchronizer.synchronize(config, dto)

    }

    @Override
    void categoryChanged(final DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt)
        def ds = syncService.getDatasource(config)
        def dto = ds.getCategory(evt.externalRef, config.parametersAsMap)
        syncService.synchronizeCategory(config, dto)

    }

    @Override
    void brandChanged(final DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt)

        def ds = syncService.getDatasource(config)
        def dto = ds.getBrand(evt.externalRef, config.parametersAsMap)
        syncService.synchronizeBrand(config, dto)

    }

    @Override
    void storeChanged(final DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt)

        def ds = syncService.getDatasource(config)
        def dto = ds.getStore(evt.externalRef, config.parametersAsMap)
        syncService.synchronizeStore(config, dto)

    }

    private ProductsSiteConfig getConfig(DataChangedEvent evt) {
        final ProductsSiteConfig config = service.getSiteConfig(evt.token)
        if (config == null) {
            throw new InvalidTokenException("Cannot find ProductSiteConfig for supplied token")
        }
        return config
    }

}
