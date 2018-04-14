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
package tools.dynamia.cms.site.menus.domain

import org.hibernate.annotations.BatchSize
import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Site
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
