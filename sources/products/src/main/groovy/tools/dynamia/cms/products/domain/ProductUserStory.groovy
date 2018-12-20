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
