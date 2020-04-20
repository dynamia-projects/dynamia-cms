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
package tools.dynamia.cms.pages

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.domain.contraints.NotEmpty

import javax.servlet.http.HttpServletRequest

/**
 *
 * @author Mario Serrano Leones
 */

class SearchForm implements Serializable {

	@NotEmpty
	private String query

    @JsonIgnore
	private HttpServletRequest request

    String getQuery() {
		return query
    }

    void setQuery(String query) {
		this.query = query
    }

    HttpServletRequest getRequest() {
		return request
    }

    void setRequest(HttpServletRequest request) {
		this.request = request
    }

}
