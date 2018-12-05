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
import tools.dynamia.cms.users.domain.User
import tools.dynamia.domain.SimpleEntity

import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Temporal

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_user_story")
class ProductUserStory extends SimpleEntity {

    @OneToOne
    @JsonIgnore
    private Product product

    @OneToOne
    @JsonIgnore
    private User user
    private long views
    private long shops

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastView
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date firstView
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastShop
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date firstShop

    Product getProduct() {
        return product
    }

    void setProduct(Product product) {
        this.product = product
    }

    User getUser() {
        return user
    }

    void setUser(User user) {
        this.user = user
    }

    long getViews() {
        return views
    }

    void setViews(long views) {
        this.views = views
    }

    long getShops() {
        return shops
    }

    void setShops(long shops) {
        this.shops = shops
    }

    Date getLastView() {
        return lastView
    }

    void setLastView(Date lastView) {
        this.lastView = lastView
    }

    Date getFirstView() {
        return firstView
    }

    void setFirstView(Date firstView) {
        this.firstView = firstView
    }

    Date getLastShop() {
        return lastShop
    }

    void setLastShop(Date lastShop) {
        this.lastShop = lastShop
    }

    Date getFirstShop() {
        return firstShop
    }

    void setFirstShop(Date firstShop) {
        this.firstShop = firstShop
    }

}
