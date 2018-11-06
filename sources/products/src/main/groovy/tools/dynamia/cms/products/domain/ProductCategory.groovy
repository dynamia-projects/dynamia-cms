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
package tools.dynamia.cms.products.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.BatchSize
import tools.dynamia.cms.core.Orderable
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.products.dto.ProductCategoryDTO
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_categories")
@BatchSize(size = 50)
class ProductCategory extends SimpleEntity implements SiteAware, Orderable {

    @OneToOne
    @NotNull
    Site site

    @ManyToOne
    @JsonIgnore
    ProductCategory parent
    @OneToMany(mappedBy = "parent")
    List<ProductCategory> subcategories = new ArrayList<>()

    @NotNull
    @NotEmpty(message = "Enter product category name")
    String name
    String alternateName

    @Column(name = "catAlias")
    String alias
    String description
    boolean active = true

    Long externalRef

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    ProductCategory relatedCategory

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @OrderBy("order")
    @JsonIgnore
    List<ProductCategoryDetail> details = new ArrayList<>()

    @Column(name = "catOrder")
    int order

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    ProductTemplate template

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    ProductTemplate alternateTemplate

    @Column(length = 5000)
    String tags
    Long productsCount

    void sync(ProductCategoryDTO dto) {
        name = dto.name
        order = dto.order
        alternateName = dto.alternateName
        active = dto.active
        description = dto.description
        externalRef = dto.externalRef
        tags = dto.tags
    }

    @Override
    String toString() {
        return name
    }

}
