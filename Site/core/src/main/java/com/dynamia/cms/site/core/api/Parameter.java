package com.dynamia.cms.site.core.api;

public interface Parameter {

	String getName();

	String getValue();

	String getExtra();

	void setName(String name);

	void setValue(String value);

	void setExtra(String extra);
}
