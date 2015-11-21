package com.dynamia.cms.site.core;

import java.io.Serializable;

public class StyleSheetResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2763610197937288114L;
	private String name;
	private String href;

	public StyleSheetResource() {

	}

	public StyleSheetResource(String name, String href) {
		super();
		this.name = name;
		this.href = href;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StyleSheetResource other = (StyleSheetResource) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
