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
package tools.dynamia.cms.site.shoppingcart.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.shoppingcart.domain.enums.ShoppingCartStatus;
import tools.dynamia.cms.site.users.domain.User;

import tools.dynamia.domain.SimpleEntity;

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "sc_shopping_carts")
public class ShoppingCart extends SimpleEntity implements SiteAware {

	private String name;
	private String title;
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp = new Date();
	@OneToOne
	private User user;
	@OneToOne
	private User customer;
	@OneToOne
	@NotNull
	private Site site;
	private int quantity;
	private BigDecimal subtotal;
	private BigDecimal totalShipmentPrice;
	private BigDecimal totalTaxes;
	private BigDecimal totalPrice;
	private BigDecimal totalUnit;
	private float shipmentPercent;

	@OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
	private List<ShoppingCartItem> items = new ArrayList<>();

	@Enumerated(EnumType.ORDINAL)
	private ShoppingCartStatus status = ShoppingCartStatus.NEW;

	private BigDecimal totalDiscount;

	public BigDecimal getTotalUnit() {
		return totalUnit;
	}

	public void setTotalUnit(BigDecimal totalUnit) {
		this.totalUnit = totalUnit;
	}

	public BigDecimal getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(BigDecimal totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public float getShipmentPercent() {
		return shipmentPercent;
	}

	public void setShipmentPercent(float shipmentPercent) {
		this.shipmentPercent = shipmentPercent;
	}

	public ShoppingCart() {
		// TODO Auto-generated constructor stub
	}

	public ShoppingCart(String name) {
		super();
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ShoppingCartStatus getStatus() {
		return status;
	}

	public void setStatus(ShoppingCartStatus status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void setSite(Site site) {
		this.site = site;
	}

	public void addItem(ShoppingCartItem item, int qty) {
		ShoppingCartItem addedItem = getItemByCode(item.getCode());
		if (addedItem != null) {
			addedItem.setQuantity(addedItem.getQuantity() + qty);
		} else {
			items.add(item);
			item.setShoppingCart(this);
		}
		compute();
	}

	public void addItem(ShoppingCartItem item) {
		addItem(item, 1);
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
			if (addedItem.getChildren() != null && !addedItem.getChildren().isEmpty()) {
				addedItem.getChildren().forEach(c -> items.remove(c));
			}
			compute();
			return true;
		}
		return false;
	}

	public void compute() {
		totalPrice = BigDecimal.ZERO;
		totalTaxes = BigDecimal.ZERO;
		totalShipmentPrice = BigDecimal.ZERO;
		totalDiscount = BigDecimal.ZERO;
		totalUnit = BigDecimal.ZERO;
		subtotal = BigDecimal.ZERO;
		quantity = 0;

		for (ShoppingCartItem item : items) {
			item.compute();
			quantity += item.getQuantity();
			totalTaxes = totalTaxes.add(item.getTaxes());
			totalShipmentPrice = totalShipmentPrice.add(item.getShipmentPrice());
			totalDiscount = totalDiscount.add(item.getDiscount());
			totalUnit = totalUnit.add(item.getUnitPrice());
			subtotal = subtotal.add(item.getTotalPrice());
		}

		if (shipmentPercent > 0) {
			totalShipmentPrice = subtotal.multiply(new BigDecimal(shipmentPercent / 100));

		}

		computeTotalOnly();
	}

	public void computeTotalOnly() {
		totalPrice = subtotal.add(totalTaxes).add(totalShipmentPrice);
	}

	public ShoppingCartItem getItemByCode(String code) {
		for (ShoppingCartItem item : items) {
			if (item.getCode().equals(code) && item.isEditable()) {
				return item;
			}
		}
		return null;
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	public User getTargetUser() {
		if (customer != null) {
			return customer;
		} else {
			return user;
		}
	}

}
