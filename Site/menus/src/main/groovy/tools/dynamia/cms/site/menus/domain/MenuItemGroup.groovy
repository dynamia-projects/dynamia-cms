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
		menuItem.setParentGroup(this)
		subitems.add(menuItem)
	}

}
