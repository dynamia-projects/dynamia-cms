/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.core

class StyleSheetResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2763610197937288114L
    private String name
    private String href

    StyleSheetResource() {

	}

    StyleSheetResource(String name, String href) {
		super()
        this.name = name
        this.href = href
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getHref() {
		return href
    }

    void setHref(String href) {
		this.href = href
    }

	@Override
    int hashCode() {
		final int prime = 31
        int result = 1
        result = prime * result + ((name == null) ? 0 : name.hashCode())
        return result
    }

	@Override
    boolean equals(Object obj) {
		if (this == obj)
			return true
        if (obj == null)
			return false
        if (getClass() != obj.class)
			return false
        StyleSheetResource other = (StyleSheetResource) obj
        if (name == null) {
			if (other.name != null)
				return false
        } else if (!name.equals(other.name))
			return false
        return true
    }

}
