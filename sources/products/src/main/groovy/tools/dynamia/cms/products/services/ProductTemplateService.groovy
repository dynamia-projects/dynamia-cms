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
