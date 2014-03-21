/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.api;

import com.dynamia.cms.site.products.dto.ProductDTO;
import com.dynamia.cms.site.products.dto.ProductBrandDTO;
import com.dynamia.cms.site.products.dto.ProductCategoryDTO;
import com.dynamia.cms.site.products.dto.StoreDTO;
import java.util.List;
import java.util.Map;

/**
 * This interface represent a product datasource that can be local or remote. It
 * can be used to implement a remote datasource to synchronize DynamiaCMS
 * database from external application like ERPs or something like that.
 *
 * @author mario
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
}
