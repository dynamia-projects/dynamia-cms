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
