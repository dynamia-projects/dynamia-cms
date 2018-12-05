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
