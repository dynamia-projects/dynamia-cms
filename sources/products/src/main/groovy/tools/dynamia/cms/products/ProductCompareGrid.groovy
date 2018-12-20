/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
