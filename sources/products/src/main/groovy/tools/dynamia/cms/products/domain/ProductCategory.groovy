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
