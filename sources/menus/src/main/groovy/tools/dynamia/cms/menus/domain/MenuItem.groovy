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
package tools.dynamia.cms.menus.domain

import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tools.dynamia.cms.core.Orderable
import tools.dynamia.cms.pages.domain.Page
import tools.dynamia.domain.SimpleEntity

import javax.persistence.*

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "mn_menuitems")
@BatchSize(size = 50)
class MenuItem extends SimpleEntity implements Serializable, Orderable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6009984090089120221L
	private String name = ""
	@OneToOne
	private Page page
	@ManyToOne
	private Menu menu
	@ManyToOne
	private MenuItem parentItem
	@ManyToOne
	private MenuItemGroup parentGroup
	private String icon
	@Column(name = "itemOrder")
	private int order
	@Column(name = "itemType")
	private String type = "default"

	@OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<MenuItemParameter> parameters = new ArrayList<>()

	@OneToMany(mappedBy = "parentItem", cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<MenuItem> subitems = new ArrayList<>()

	@OneToMany(mappedBy = "parentItem", cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<MenuItemGroup> itemsGroups = new ArrayList<>()

	private String styleClass = ""
	private String href
	private String title = ""
	private String subtitle = ""
	private String target = ""
	private String backgroundImage = ""

	MenuItem() {
		// TODO Auto-generated constructor stub
	}

	MenuItem(String name, Page page) {
		super()
		this.name = name
		this.page = page
	}

	MenuItem(String name, String href) {
		super()
		this.name = name
		this.href = href
	}

	MenuItem(String name, Page page, String icon) {
		super()
		this.name = name
		this.page = page
		this.icon = icon
	}

	MenuItem(String name, String href, String icon) {
		super()
		this.name = name
		this.icon = icon
		this.href = href
	}

	String getBackgroundImage() {
		return backgroundImage
	}

	void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage
	}

	String getSubtitle() {
		return subtitle
	}

	void setSubtitle(String subtitle) {
		this.subtitle = subtitle
	}

	MenuItemGroup getParentGroup() {
		return parentGroup
	}

	void setParentGroup(MenuItemGroup parentGroup) {
		this.parentGroup = parentGroup
	}

	String getTitle() {
		return title
	}

	void setTitle(String title) {
		this.title = title
	}

	String getStyleClass() {
		if (styleClass == null) {
			styleClass = ""
		}
		return styleClass
	}

	void setStyleClass(String styleClass) {
		this.styleClass = styleClass
	}

	void setHref(String href) {
		this.href = href
	}

	MenuItem getParentItem() {
		return parentItem
	}

	void setParentItem(MenuItem parentItem) {
		this.parentItem = parentItem
	}

	List<MenuItem> getSubitems() {
		return subitems
	}

	void setSubitems(List<MenuItem> subitems) {
		this.subitems = subitems
	}

	List<MenuItemGroup> getItemsGroups() {
		return itemsGroups
	}

	void setItemsGroups(List<MenuItemGroup> itemsGroups) {
		this.itemsGroups = itemsGroups
	}

	String getIcon() {
		return icon
	}

	void setIcon(String icon) {
		this.icon = icon
	}

	int getOrder() {
		return order
	}

	void setOrder(int order) {
		this.order = order
	}

	Menu getMenu() {
		return menu
	}

	void setMenu(Menu menu) {
		this.menu = menu
	}

	String getName() {
		return name
	}

	void setName(String name) {
		this.name = name
	}

	Page getPage() {
		return page
	}

	void setPage(Page page) {
		this.page = page

	}

	String getType() {
		return type
	}

	void setType(String type) {
		this.type = type
	}

	List<MenuItemParameter> getParameters() {
		return parameters
	}

	void setParameters(List<MenuItemParameter> parameters) {
		this.parameters = parameters
	}

	String getHref() {
		checkHref()
		return href
	}

	private void checkHref() {
		if (page != null) {
			href = "/" + page.alias
		}
	}

	void addMenuItem(MenuItem subitem) {
		if (subitem != this) {
            subitem.parentItem = this
            subitems.add(subitem)
		}
	}

	void addMenuItemGroup(MenuItemGroup group) {
        group.parentItem = this
        itemsGroups.add(group)
	}

	String getTarget() {
		return target
	}

	void setTarget(String target) {
		this.target = target
	}

	MenuItemParameter getParameter(String name) {
		if (parameters != null) {
			for (MenuItemParameter param : (parameters)) {
				if (param.name.equalsIgnoreCase(name)) {
					return param
				}
			}
		}
		return null
	}

	MenuItem clone() {
		MenuItem clone = new MenuItem()
		clone.backgroundImage = backgroundImage
		clone.href = href
		clone.icon = icon
		clone.name = name + " (copy)"
		clone.order = order + 1
		clone.page = page
		clone.styleClass = styleClass
		clone.subtitle = subtitle
		clone.target = target
		clone.title = title + " (copy)"
		clone.type = type
		clone.menu = menu

		for (MenuItemParameter parameter : parameters) {
			MenuItemParameter cloneParam = parameter.clone()
			clone.parameters.add(cloneParam)
            cloneParam.menuItem = clone
        }

		return clone

	}

	@Override
	String toString() {
		return name
	}

}
