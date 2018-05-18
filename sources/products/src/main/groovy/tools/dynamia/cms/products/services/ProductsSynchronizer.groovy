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
package tools.dynamia.cms.products.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.cms.products.dto.ProductBrandDTO
import tools.dynamia.cms.products.dto.ProductCategoryDTO
import tools.dynamia.cms.products.dto.ProductDTO
import tools.dynamia.cms.products.dto.RelatedProductDTO
import tools.dynamia.cms.products.dto.StoreDTO
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.scheduling.SchedulerUtil
import tools.dynamia.integration.scheduling.Task

/**
 *
 * @author Mario Serrano Leones
 */
@Service
class ProductsSynchronizer {

	@Autowired
	private ProductsSyncService service

    @Autowired
	private ProductsService productsService

    @Autowired
	private SiteService siteService

    @Autowired
	private CrudService crudService

    private LoggingService logger = new SLF4JLoggingService(ProductsSynchronizer.class)

    void synchronize(ProductsSiteConfig siteConfig) {
		siteConfig = crudService.reload(siteConfig)

        if (!siteConfig.synchronizationEnabled) {
			logger.warn("Product Synchronization is NOT enabled for  " + siteConfig.site)
            return
        }
		final ProductsSiteConfig siteCfg = siteConfig

        logger.info("Starting Products Sync for " + siteCfg.site)

        logger.info("Brands")
        List<ProductBrandDTO> brands = service.synchronizeBrands(siteCfg)

        logger.info("Stores")
        List<StoreDTO> stores = service.synchronizeStores(siteCfg)

        try {
			service.deleteStoreContactsNoInList(siteCfg,stores)

        }catch (Exception e){

		}

		logger.info("Categories")
        List<ProductCategoryDTO> categories = service.synchronizeCategories(siteCfg)
        service.disableCategoriesNoInList(siteCfg, categories)

        logger.info("Products")
        List<ProductDTO> products = service.synchronizeProducts(siteCfg)
        for (ProductDTO productDTO : products) {
			if (siteCfg.syncProductDetails) {
				logger.info("-Product Details for " + productDTO.name)
                service.syncProductDetails(siteCfg, productDTO)
            }

			if (siteCfg.syncStockDetails) {
				logger.info("-Product Stock for " + productDTO.name)
                service.syncProductStockDetails(siteCfg, productDTO)
            }

			if (siteCfg.syncProductCreditPrices) {
				logger.info("-Product Prices for " + productDTO.name)
                service.syncProductCreditPrices(siteCfg, productDTO)
            }
		}

		service.disableProductsNoInList(siteCfg, products)

        logger.info("Related Products")
        List<RelatedProductDTO> relatedProducts = service.synchronizeRelatedProducts(siteCfg)
        service.disableRelatedProductsNoInList(siteCfg, relatedProducts)

        logger.info("Compute product count by category")
        productsService.computeProductCountByCategory(siteCfg.site)

        if (siteCfg.syncProductImages) {
			SchedulerUtil.run(new Task("Images Downloader") {
				@Override
                void doWork() {

					for (ProductBrandDTO brandDTO : brands) {
						logger.info("Downloading Brand Images for " + brandDTO.name)
                        service.downloadBrandImages(siteCfg, brandDTO)
                    }

					for (StoreDTO storeDTO : stores) {
						logger.info("Downloading Store Images for " + storeDTO.name)
                        service.downloadStoreImages(siteCfg, storeDTO)
                    }

					for (ProductDTO productDTO : products) {
						logger.info("Downloading Product Images for " + productDTO.name)
                        service.downloadProductImages(siteCfg, productDTO)
                    }
					logger.info("Images downloading completed.")
                }
			})
        }

		service.update(siteCfg)
        logger.info("Sync Completed for " + siteCfg.site)

        siteService.clearCache(siteCfg.site)
    }

    void synchronize(ProductsSiteConfig siteCfg, ProductDTO dto) {
		try {

			service.synchronizeProduct(siteCfg, dto)
            service.syncProductDetails(siteCfg, dto)
            service.syncProductStockDetails(siteCfg, dto)
            service.syncProductCreditPrices(siteCfg, dto)
            service.downloadProductImages(siteCfg, dto)
            siteService.clearCache(siteCfg.site)
        } catch (Exception e) {
			logger.error("Error Syncronizing product " + dto.name + " for SITE: " + siteCfg.site, e)
        }
	}
}
