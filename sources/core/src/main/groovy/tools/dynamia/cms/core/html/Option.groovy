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
package tools.dynamia.cms.core.html

class Option {
	private String name
    private String value
    private boolean selected

    Option() {
		// TODO Auto-generated constructor stub
	}

    Option(String name, String value, boolean selected) {
		super()
        this.name = name
        this.value = value
        this.selected = selected
    }

    Option(String name, String value) {
		super()
        this.name = name
        this.value = value
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getValue() {
		return value
    }

    void setValue(String value) {
		this.value = value
    }

    boolean isSelected() {
		return selected
    }

    void setSelected(boolean selected) {
		this.selected = selected
    }

    static List<Option> buildFromArray(String[] values, String selected) {
		if (selected == null) {
			selected = ""
        }

		List<Option> options = new ArrayList<>()

        for (String value : values) {
			options.add(new Option(value, value, selected.equals(value)))
        }

		return options
    }
}
