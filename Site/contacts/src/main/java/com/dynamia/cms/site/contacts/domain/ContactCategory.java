package com.dynamia.cms.site.contacts.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dynamia.cms.site.core.Orderable;
import com.dynamia.cms.site.core.domain.SiteSimpleEntity;
import com.dynamia.tools.domain.contraints.NotEmpty;

@Entity
@Table(name = "cts_categories")
public class ContactCategory extends SiteSimpleEntity implements Orderable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5084812821635888414L;
	@NotEmpty(message = "Enter Contact category name")
	private String name;
	@Column(length = 1000)
	private String description;
	private boolean active = true;
	@Column(name="catOrder")
	private int order;
	private String defaultEmail;

	public String getDefaultEmail() {
		return defaultEmail;
	}

	public void setDefaultEmail(String defaultEmail) {
		this.defaultEmail = defaultEmail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return name;
	}

}
