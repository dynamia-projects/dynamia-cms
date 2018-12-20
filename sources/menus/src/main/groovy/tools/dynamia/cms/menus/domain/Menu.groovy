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
package tools.dynamia.cms.menus.domain

import org.hibernate.annotations.BatchSize
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "mn_menus")
@BatchSize(size = 50)
class Menu extends SimpleEntity implements Serializable, SiteAware {

	@NotEmpty
	private String name
	@Column(name = "menuAlias")
	private String alias
	@OneToOne
	@NotNull
	private Site site
	@OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("order")
	private List<MenuItem> items = new ArrayList<>()
	@Column(length = 1000)
	private String description
	private String styleClass = ""
	private String menuItemStyleClass = ""

	Menu() {
	}

	Menu(String name, String alias) {
		this.name = name
		this.alias = alias
	}

	String getDescription() {
		return description
	}

	void setDescription(String description) {
		this.description = description
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

	String getMenuItemStyleClass() {
		if (menuItemStyleClass == null) {
			menuItemStyleClass = ""
		}
		return menuItemStyleClass
	}

	void setMenuItemStyleClass(String menuItemStyleClass) {
		this.menuItemStyleClass = menuItemStyleClass
	}

	Site getSite() {
		return site
	}

	void setSite(Site site) {
		this.site = site
	}

	String getName() {
		return name
	}

	void setName(String name) {
		this.name = name
	}

	String getAlias() {
		return alias
	}

	void setAlias(String alias) {
		this.alias = alias
	}

	List<MenuItem> getItems() {
		return items
	}

	void setItems(List<MenuItem> items) {
		this.items = items
	}

	@Override
	String toString() {
		return name
	}

}
