package com.dynamia.cms.site.core;

import java.io.Serializable;

public class JavaScriptResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 296681701178058546L;

	private String name;
	private String src;

	public JavaScriptResource() {

	}

	public JavaScriptResource(String name, String src) {
		super();
		this.name = name;
		this.src = src;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
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
		JavaScriptResource other = (JavaScriptResource) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
