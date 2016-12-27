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
package tools.dynamia.cms.admin.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import tools.dynamia.cms.site.products.domain.ProductsSiteConfig;
import tools.dynamia.cms.site.products.services.ProductsSynchronizer;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.domain.services.CrudService;

/**
 *
 * @author Mario Serrano Leones
 */
@Service
public class ProductsAutoSynchronizer {

	@Autowired
	private ProductsSynchronizer synchronizer;
	@Autowired
	private CrudService crudService;

	private LoggingService logger = new SLF4JLoggingService(ProductsAutoSynchronizer.class);

	@Scheduled(fixedDelay = 2 * 60 * 60 * 1000)
	public void sync() {
		List<ProductsSiteConfig> configs = crudService.find(ProductsSiteConfig.class, "site.offline", false);
		for (ProductsSiteConfig config : configs) {
			try {
				synchronizer.synchronize(config);
			} catch (Throwable e) {
				logger.error("Error autosyncrhozing Products Site: " + config.getSite(), e);
				e.printStackTrace();
			}
		}

	}

}
