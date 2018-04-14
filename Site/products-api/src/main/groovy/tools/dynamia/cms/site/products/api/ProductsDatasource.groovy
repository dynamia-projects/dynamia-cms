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
package tools.dynamia.cms.site.products.api;

import tools.dynamia.cms.site.products.dto.ProductDTO;
import tools.dynamia.cms.site.products.dto.ProductBrandDTO;
import tools.dynamia.cms.site.products.dto.ProductCategoryDTO;
import tools.dynamia.cms.site.products.dto.RelatedProductDTO;
import tools.dynamia.cms.site.products.dto.StoreDTO;
import java.util.List;
import java.util.Map;

/**
 * This interface represent a product datasource that can be local or remote. It
 * can be used to implement a remote datasource to synchronize DynamiaCMS
 * database from external application like ERPs or something like that.
 *
 * @author Mario Serrano Leones
 */
public interface ProductsDatasource {

    public List<ProductCategoryDTO> getCategories(Map<String, String> params);

    public ProductCategoryDTO getCategory(Long externalRef, Map<String, String> params);

    public List<ProductDTO> getProducts(Map<String, String> params);

    public ProductDTO getProduct(Long externalRef, Map<String, String> params);

    public List<ProductBrandDTO> getBrands(Map<String, String> params);

    public ProductBrandDTO getBrand(Long externalRef, Map<String, String> params);

    public List<StoreDTO> getStores(Map<String, String> params);

    public StoreDTO getStore(Long externalRef, Map<String, String> params);

    public List<RelatedProductDTO> getRelatedProducts(Map<String, String> params);
}
