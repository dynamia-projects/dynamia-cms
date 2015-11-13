/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.menus.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;

import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.contraints.NotEmpty;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "mn_menus")
public class Menu extends SimpleEntity implements Serializable, SiteAware {

	@NotEmpty
	private String name;
	@Column(name = "menuAlias")
	private String alias;
	@OneToOne
	@NotNull
	private Site site;
	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("order")
	private List<MenuItem> items = new ArrayList<>();
	@Column(length = 1000)
	private String description;
	private String styleClass = "";
	private String menuItemStyleClass = "";

	public Menu() {
	}

	public Menu(String name, String alias) {
		this.name = name;
		this.alias = alias;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStyleClass() {
		if (styleClass == null) {
			styleClass = "";
		}
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getMenuItemStyleClass() {
		if (menuItemStyleClass == null) {
			menuItemStyleClass = "";
		}
		return menuItemStyleClass;
	}

	public void setMenuItemStyleClass(String menuItemStyleClass) {
		this.menuItemStyleClass = menuItemStyleClass;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public List<MenuItem> getItems() {
		return items;
	}

	public void setItems(List<MenuItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return name;
	}
}
