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
