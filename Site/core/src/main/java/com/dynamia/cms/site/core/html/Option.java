package com.dynamia.cms.site.core.html;

import java.util.ArrayList;
import java.util.List;

public class Option {
	private String name;
	private String value;
	private boolean selected;

	public Option() {
		// TODO Auto-generated constructor stub
	}

	public Option(String name, String value, boolean selected) {
		super();
		this.name = name;
		this.value = value;
		this.selected = selected;
	}

	public Option(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public static List<Option> buildFromArray(String[] values, String selected) {
		if (selected == null) {
			selected = "";
		}

		List<Option> options = new ArrayList<>();

		for (String value : values) {
			options.add(new Option(value, value, selected.equals(value)));
		}

		return options;
	}
}
