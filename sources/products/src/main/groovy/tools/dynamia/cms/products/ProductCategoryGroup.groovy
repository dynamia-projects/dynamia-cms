/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package tools.dynamia.cms.products

import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.domain.ProductCategory
import tools.dynamia.commons.collect.HashSetMultiMap
import tools.dynamia.commons.collect.MultiMap

class ProductCategoryGroup implements Serializable {

    private ProductCategory category
    private List<Product> products

    ProductCategory getCategory() {
        return category
    }

    void setCategory(ProductCategory category) {
        this.category = category
    }

    List<Product> getProducts() {
        return products
    }

    void setProducts(List<Product> products) {
        this.products = products
    }

    static List<ProductCategoryGroup> groupByCategory(List<Product> products) {
        List<ProductCategoryGroup> groups = new ArrayList<>()
        if (products != null) {
            MultiMap<ProductCategory, Product> maps = new HashSetMultiMap<>()
            products.forEach { p -> maps.put(p.category, p) }


            maps.forEach { k, v ->
                ProductCategoryGroup grp = new ProductCategoryGroup()
                grp.category = k
                grp.products = new ArrayList<>(v)
                groups.add(grp)
            }

            groups.sort(Comparator.comparing { g -> g.getCategory().getName() })
        }
        return groups
    }


}
