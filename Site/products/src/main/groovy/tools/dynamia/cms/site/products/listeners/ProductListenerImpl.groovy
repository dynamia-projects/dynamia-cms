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
package tools.dynamia.cms.site.products.listeners

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.products.api.DataChangedEvent
import tools.dynamia.cms.site.products.api.InvalidTokenException
import tools.dynamia.cms.site.products.api.ProductsDatasource
import tools.dynamia.cms.site.products.api.ProductsListener
import tools.dynamia.cms.site.products.domain.ProductsSiteConfig
import tools.dynamia.cms.site.products.dto.ProductBrandDTO
import tools.dynamia.cms.site.products.dto.ProductCategoryDTO
import tools.dynamia.cms.site.products.dto.ProductDTO
import tools.dynamia.cms.site.products.dto.StoreDTO
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.cms.site.products.services.ProductsSyncService
import tools.dynamia.cms.site.products.services.ProductsSynchronizer

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

        ProductsDatasource ds = syncService.getDatasource(config)
        ProductDTO dto = ds.getProduct(evt.getExternalRef(), config.getParametersAsMap())
        synchronizer.synchronize(config, dto)

    }

    @Override
    void categoryChanged(final DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt)
        ProductsDatasource ds = syncService.getDatasource(config)
        ProductCategoryDTO dto = ds.getCategory(evt.getExternalRef(), config.getParametersAsMap())
        syncService.synchronizeCategory(config, dto)

    }

    @Override
    void brandChanged(final DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt)

        ProductsDatasource ds = syncService.getDatasource(config)
        ProductBrandDTO dto = ds.getBrand(evt.getExternalRef(), config.getParametersAsMap())
        syncService.synchronizeBrand(config, dto)

    }

    @Override
    void storeChanged(final DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt)

        ProductsDatasource ds = syncService.getDatasource(config)
        StoreDTO dto = ds.getStore(evt.getExternalRef(), config.getParametersAsMap())
        syncService.synchronizeStore(config, dto)

    }

    private ProductsSiteConfig getConfig(DataChangedEvent evt) {
        final ProductsSiteConfig config = service.getSiteConfig(evt.getToken())
        if (config == null) {
            throw new InvalidTokenException("Cannot find ProductSiteConfig for supplied token")
        }
        return config
    }

}