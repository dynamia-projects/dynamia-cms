/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author mario_2
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
