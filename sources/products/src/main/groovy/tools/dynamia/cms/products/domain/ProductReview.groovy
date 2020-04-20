/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
import tools.dynamia.cms.core.domain.SiteBaseEntity
import tools.dynamia.cms.users.domain.User

import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

/**
 * Created by mario on 12/07/2017.
 */
@Entity
@Table(name = "prd_reviews", uniqueConstraints = @UniqueConstraint(columnNames = ["product_id", "user_id"]))
class ProductReview extends SiteBaseEntity {

    @OneToOne
    @NotNull
    User user

    @ManyToOne
    @NotNull
    @JsonIgnore
    Product product


    @Column(length = 1000)
    String comment
    @Min(1L)
    @Max(5L)
    int stars = 5

    boolean verified
    String document
    boolean incomplete

    
    @Override
    String toString() {
        return String.format("%s review %s with %s stars and say %s", user, product, stars, comment)
    }
}
