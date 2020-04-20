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
package tools.dynamia.cms.products.api

import tools.dynamia.cms.products.dto.ProductBrandDTO
import tools.dynamia.cms.products.dto.ProductCategoryDTO
import tools.dynamia.cms.products.dto.ProductDTO
import tools.dynamia.cms.products.dto.RelatedProductDTO
import tools.dynamia.cms.products.dto.StoreDTO

/**
 * This interface represent a product datasource that can be local or remote. It
 * can be used to implement a remote datasource to synchronize boot
 * database from external application like ERPs or something like that.
 *
 * @author Mario Serrano Leones
 */
interface ProductsDatasource {

    List<ProductCategoryDTO> getCategories(Map<String, String> params)

    ProductCategoryDTO getCategory(Long externalRef, Map<String, String> params)

    List<ProductDTO> getProducts(Map<String, String> params)

    ProductDTO getProduct(Long externalRef, Map<String, String> params)

    List<ProductBrandDTO> getBrands(Map<String, String> params)

    ProductBrandDTO getBrand(Long externalRef, Map<String, String> params)

    List<StoreDTO> getStores(Map<String, String> params)

    StoreDTO getStore(Long externalRef, Map<String, String> params)

    List<RelatedProductDTO> getRelatedProducts(Map<String, String> params)
}
