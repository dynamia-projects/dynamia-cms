package com.dynamia.cms.site.menus.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.dynamia.tools.domain.SimpleEntity;

@Entity
@Table(name = "mn_menuitems_groups")
public class MenuItemGroup extends SimpleEntity {

	private String name;
	@ManyToOne
	private MenuItem parentItem;
	@OneToMany(mappedBy = "parentGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<MenuItem> subitems = new ArrayList<>();

	public MenuItemGroup() {
		// TODO Auto-generated constructor stub
	}

	public MenuItemGroup(String name) {
		super();
		this.name = name;	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public void addMenuItem(MenuItem menuItem){
		menuItem.setParentGroup(this);
		subitems.add(menuItem);
	}

}
