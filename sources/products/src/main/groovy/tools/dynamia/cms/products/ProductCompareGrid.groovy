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
package tools.dynamia.cms.products

import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.domain.ProductDetail
import tools.dynamia.cms.products.services.ProductsService
import tools.dynamia.commons.StringUtils
import tools.dynamia.commons.collect.ArrayListMultiMap
import tools.dynamia.commons.collect.MultiMap

/**
 *
 * @author Mario Serrano Leones
 */
class ProductCompareGrid implements Serializable {

    private ProductsService service
    private Set<String> features = new TreeSet<>()
    private MultiMap<String, ProductDetail> data = new ArrayListMultiMap<String, ProductDetail>()
    private List<Product> products

    ProductCompareGrid(ProductsService service, List<Product> products) {
        this.service = service
        this.products = products
    }

    void loadData() {
        List<ProductDetail> allDetails = service.getProductsDetails(products)
        for (ProductDetail productDetail : allDetails) {
            features.add(cleanFeature(productDetail.name))
        }

        for (Product product : products) {
            List<ProductDetail> productDetails = getDetails(product, allDetails)
            for (String name : features) {
                ProductDetail det = getDetail(name, productDetails)
                data.put(name, det)
            }
        }

    }

    MultiMap<String, ProductDetail> getData() {
        return data
    }

    List<Product> getProducts() {
        return products
    }

    Set<String> getFeatures() {
        return features
    }

    private List<ProductDetail> getDetails(Product product, List<ProductDetail> allDetails) {
        List<ProductDetail> details = new ArrayList<>()
        for (ProductDetail det : allDetails) {
            if (det.product.equals(product)) {
                details.add(det)
            }
        }
        return details
    }

    private ProductDetail getDetail(String name, List<ProductDetail> productDetails) {
        for (ProductDetail productDetail : productDetails) {
            if (cleanFeature(productDetail.name).equals(name)) {
                return productDetail
            }
        }
        return null
    }

    private String cleanFeature(String name) {
        return StringUtils.capitalize(name.trim().toLowerCase())
    }
}
