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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.dynamia.cms.site.core.Orderable;
import com.dynamia.cms.site.pages.domain.Page;

import tools.dynamia.domain.SimpleEntity;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "mn_menuitems")
public class MenuItem extends SimpleEntity implements Serializable, Orderable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6009984090089120221L;
	private String name = "";
	@OneToOne
	private Page page;
	@ManyToOne
	private Menu menu;
	@ManyToOne
	private MenuItem parentItem;
	@ManyToOne
	private MenuItemGroup parentGroup;
	private String icon;
	@Column(name = "itemOrder")
	private int order;
	@Column(name = "itemType")
	private String type = "default";
	@OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<MenuItemParameter> parameters = new ArrayList<>();
	@OneToMany(mappedBy = "parentItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<MenuItem> subitems = new ArrayList<>();
	@OneToMany(mappedBy = "parentItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<MenuItemGroup> itemsGroups = new ArrayList<>();
	private String styleClass = "";
	private String href;
	private String title = "";
	private String subtitle = "";
	private String target = "";
	private String backgroundImage = "";

	public MenuItem() {
		// TODO Auto-generated constructor stub
	}

	public MenuItem(String name, Page page) {
		super();
		this.name = name;
		this.page = page;
	}

	public MenuItem(String name, String href) {
		super();
		this.name = name;
		this.href = href;
	}

	public MenuItem(String name, Page page, String icon) {
		super();
		this.name = name;
		this.page = page;
		this.icon = icon;
	}

	public MenuItem(String name, String href, String icon) {
		super();
		this.name = name;
		this.icon = icon;
		this.href = href;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public MenuItemGroup getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(MenuItemGroup parentGroup) {
		this.parentGroup = parentGroup;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public void setHref(String href) {
		this.href = href;
	}

	public MenuItem getParentItem() {
		return parentItem;
	}

	public void setParentItem(MenuItem parentItem) {
		this.parentItem = parentItem;
	}

	public List<MenuItem> getSubitems() {
		return subitems;
	}

	public void setSubitems(List<MenuItem> subitems) {
		this.subitems = subitems;
	}

	public List<MenuItemGroup> getItemsGroups() {
		return itemsGroups;
	}

	public void setItemsGroups(List<MenuItemGroup> itemsGroups) {
		this.itemsGroups = itemsGroups;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<MenuItemParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<MenuItemParameter> parameters) {
		this.parameters = parameters;
	}

	public String getHref() {
		checkHref();
		return href;
	}

	private void checkHref() {
		if (page != null) {
			href = "/" + page.getAlias();
		}
	}

	public void addMenuItem(MenuItem subitem) {
		if (subitem != this) {
			subitem.setParentItem(this);
			subitems.add(subitem);
		}
	}

	public void addMenuItemGroup(MenuItemGroup group) {
		group.setParentItem(this);
		itemsGroups.add(group);
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public MenuItemParameter getParameter(String name) {
		if (getParameters() != null) {
			for (MenuItemParameter param : getParameters()) {
				if (param.getName().equalsIgnoreCase(name)) {
					return param;
				}
			}
		}
		return null;
	}

	public MenuItem clone() {
		MenuItem clone = new MenuItem();
		clone.backgroundImage = backgroundImage;
		clone.href = href;
		clone.icon = icon;
		clone.name = name+" (copy)";
		clone.order = order + 1;
		clone.page = page;
		clone.styleClass = styleClass;
		clone.subtitle = subtitle;
		clone.target = target;
		clone.title = title+" (copy)";
		clone.type = type;
		clone.menu = menu;

		for (MenuItemParameter parameter : parameters) {
			MenuItemParameter cloneParam = parameter.clone();
			clone.parameters.add(cloneParam);
			cloneParam.setMenuItem(clone);
		}

		return clone;

	}
	
	@Override
	public String toString() {
		return getName();
	}

}
