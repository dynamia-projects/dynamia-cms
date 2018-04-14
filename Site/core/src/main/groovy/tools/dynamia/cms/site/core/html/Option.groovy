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
package tools.dynamia.cms.site.core.html;

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
