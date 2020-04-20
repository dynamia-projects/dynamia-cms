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

import tools.dynamia.domain.SimpleEntity

import javax.persistence.*

@Entity
@Table(name = "mn_menuitems_groups")
class MenuItemGroup extends SimpleEntity {

	private String name
	@ManyToOne
	private MenuItem parentItem
	@OneToMany(mappedBy = "parentGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<MenuItem> subitems = new ArrayList<>()
	private String href

	MenuItemGroup() {
		// TODO Auto-generated constructor stub
	}

	MenuItemGroup(String name) {
		super()
		this.name = name
	}

	String getHref() {
		return href
	}

	void setHref(String href) {
		this.href = href
	}

	String getName() {
		return name
	}

	void setName(String name) {
		this.name = name
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

	void addMenuItem(MenuItem menuItem) {
        menuItem.parentGroup = this
        subitems.add(menuItem)
	}

}
