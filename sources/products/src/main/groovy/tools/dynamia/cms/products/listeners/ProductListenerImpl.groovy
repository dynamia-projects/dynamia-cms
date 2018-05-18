/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.products.listeners

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.services.ProductsSyncService

/**
 *
 * @author Mario Serrano Leones
 */
class ProductListenerImpl implements tools.dynamia.cms.products.api.ProductsListener {

    @Autowired
    private ProductsSyncService syncService

    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

    @Autowired
    private tools.dynamia.cms.products.services.ProductsSynchronizer synchronizer

    @Override
    void productChanged(final tools.dynamia.cms.products.api.DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt)

        tools.dynamia.cms.products.api.ProductsDatasource ds = syncService.getDatasource(config)
        tools.dynamia.cms.products.dto.ProductDTO dto = ds.getProduct(evt.externalRef, config.parametersAsMap)
        synchronizer.synchronize(config, dto)

    }

    @Override
    void categoryChanged(final tools.dynamia.cms.products.api.DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt)
        tools.dynamia.cms.products.api.ProductsDatasource ds = syncService.getDatasource(config)
        tools.dynamia.cms.products.dto.ProductCategoryDTO dto = ds.getCategory(evt.externalRef, config.parametersAsMap)
        syncService.synchronizeCategory(config, dto)

    }

    @Override
    void brandChanged(final tools.dynamia.cms.products.api.DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt)

        tools.dynamia.cms.products.api.ProductsDatasource ds = syncService.getDatasource(config)
        tools.dynamia.cms.products.dto.ProductBrandDTO dto = ds.getBrand(evt.externalRef, config.parametersAsMap)
        syncService.synchronizeBrand(config, dto)

    }

    @Override
    void storeChanged(final tools.dynamia.cms.products.api.DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt)

        tools.dynamia.cms.products.api.ProductsDatasource ds = syncService.getDatasource(config)
        tools.dynamia.cms.products.dto.StoreDTO dto = ds.getStore(evt.externalRef, config.parametersAsMap)
        syncService.synchronizeStore(config, dto)

    }

    private ProductsSiteConfig getConfig(tools.dynamia.cms.products.api.DataChangedEvent evt) {
        final ProductsSiteConfig config = service.getSiteConfig(evt.token)
        if (config == null) {
            throw new tools.dynamia.cms.products.api.InvalidTokenException("Cannot find ProductSiteConfig for supplied token")
        }
        return config
    }

}
