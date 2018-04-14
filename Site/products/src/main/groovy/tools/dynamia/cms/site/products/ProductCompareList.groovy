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
package tools.dynamia.cms.site.products

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tools.dynamia.cms.site.products.domain.Product
import tools.dynamia.commons.StringUtils

/**
 *
 * @author Mario Serrano Leones
 */
@Component
@Scope("session")
class ProductCompareList implements Serializable {

    private List<Product> products = new ArrayList<>()

    void add(Product product) {
        if (product != null && !products.contains(product)) {
            products.add(product)
        }
    }

    void remove(Product product) {
        products.remove(product)
    }

    List<Product> getProducts() {
        return products
    }

    @Override
    String toString() {
        List<Long> ids = new ArrayList<>()
        for (Product product : products) {
            ids.add(product.id)
        }
        return StringUtils.arrayToCommaDelimitedString(ids.toArray())
    }

    void clear() {
        products = new ArrayList<>()
    }
}
