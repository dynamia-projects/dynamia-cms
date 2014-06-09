/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.domains;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.users.domain.User;
import com.dynamia.tools.domain.SimpleEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author mario_2
 */
@Entity
@Table(name = "sc_shopping_carts")
public class ShoppingCart extends SimpleEntity implements SiteAware {

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp = new Date();
    @OneToOne
    private User user;
    @OneToOne
    @NotNull
    private Site site;
    private int quantity;
    private BigDecimal subtotal;
    private BigDecimal totalShipmentPrice;
    private BigDecimal totalTaxes;
    private BigDecimal totalPrice;
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    private List<ShoppingCartItem> items = new ArrayList<>();

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalShipmentPrice() {
        return totalShipmentPrice;
    }

    public void setTotalShipmentPrice(BigDecimal totalShipmentPrice) {
        this.totalShipmentPrice = totalShipmentPrice;
    }

    public BigDecimal getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(BigDecimal totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCartItem> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void setSite(Site site) {
        this.site = site;
    }

    public void addItem(ShoppingCartItem item) {
        ShoppingCartItem addedItem = getItemByCode(item.getCode());
        if (addedItem != null) {
            addedItem.setQuatity(addedItem.getQuatity() + 1);
        } else {
            items.add(item);
            item.setShoppingCart(this);
        }
        compute();
    }

    public boolean removeItem(ShoppingCartItem item) {
        ShoppingCartItem addedItem = getItemByCode(item.getCode());
        if (addedItem != null) {
            addedItem.setShoppingCart(null);
            items.remove(addedItem);
            compute();
            return true;
        }
        return false;
    }

    public boolean removeItem(String code) {
        ShoppingCartItem addedItem = getItemByCode(code);
        if (addedItem != null) {
            addedItem.setShoppingCart(null);
            items.remove(addedItem);
            compute();
            return true;
        }
        return false;
    }

    public void compute() {
        totalPrice = BigDecimal.ZERO;
        totalTaxes = BigDecimal.ZERO;
        totalShipmentPrice = BigDecimal.ZERO;
        subtotal = BigDecimal.ZERO;
        quantity = 0;

        for (ShoppingCartItem item : items) {
            item.compute();
            quantity += item.getQuatity();
            totalTaxes = totalTaxes.add(item.getTaxes());
            totalShipmentPrice = totalShipmentPrice.add(item.getShipmentPrice());
            subtotal = subtotal.add(item.getTotalPrice());
        }
        totalPrice = subtotal.add(totalTaxes).add(totalShipmentPrice);
    }

    public ShoppingCartItem getItemByCode(String code) {
        for (ShoppingCartItem item : items) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

}
