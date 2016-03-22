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
package com.dynamia.cms.site.products.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import com.dynamia.cms.site.users.domain.User;

import tools.dynamia.domain.SimpleEntity;

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "prd_user_story")
public class ProductUserStory extends SimpleEntity {

    @OneToOne
    private Product product;

    @OneToOne
    private User user;
    private long views;
    private long shops;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastView;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date firstView;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastShop;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date firstShop;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getShops() {
        return shops;
    }

    public void setShops(long shops) {
        this.shops = shops;
    }

    public Date getLastView() {
        return lastView;
    }

    public void setLastView(Date lastView) {
        this.lastView = lastView;
    }

    public Date getFirstView() {
        return firstView;
    }

    public void setFirstView(Date firstView) {
        this.firstView = firstView;
    }

    public Date getLastShop() {
        return lastShop;
    }

    public void setLastShop(Date lastShop) {
        this.lastShop = lastShop;
    }

    public Date getFirstShop() {
        return firstShop;
    }

    public void setFirstShop(Date firstShop) {
        this.firstShop = firstShop;
    }

}
