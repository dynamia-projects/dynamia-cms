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
package tools.dynamia.cms.site.products.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.products.dto.ProductStockDTO
import tools.dynamia.domain.SimpleEntity

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_stocks")
class ProductStock extends SimpleEntity implements SiteAware {

    @OneToOne
    @NotNull
    Site site

    @NotNull
    @ManyToOne
    @JsonIgnore
    Product product
    @OneToOne
    @NotNull
    Store store
    @Min(0L)
    @JsonIgnore
    long stock

    void sync(ProductStockDTO dto) {
        stock = dto.stock
    }

}
