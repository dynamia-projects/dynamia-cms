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

import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.domain.ProductTemplate

/**
 *
 * @author Mario Serrano Leones
 */
interface ProductTemplateService {

    /**
     * Get an enabled product template from product itself or product category
     * or product category parent. If product template is not enabled return
     * null
     *
     * @param product
     * @return
     */
    ProductTemplate getTemplate(Product product)

    /**
     * Check if product has an enabled template. Internally it just check if
     * getTemplate(product) result is not null
     *
     * @param product
     * @return
     */
    boolean hasTemplate(Product product)

    /**
     * Process product template using VelocityTemplate Engine
     *
     * @param product
     * @return
     */
    String processTemplate(Product product, Map<String, Object> templateModel)

    void loadDefaultTemplateModel(Product product, Map<String, Object> templateModel)

    String processAlternateTemplate(Product product, Map<String, Object> templateModel)

}
