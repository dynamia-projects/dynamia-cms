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
package tools.dynamia.cms.pages.api
/**
 *
 * @author Mario Serrano Leones
 */
class SearchResult implements Serializable {

    private String viewName = "site/page"
    private final Map<String, Object> entries = new HashMap<>()
    private boolean keepSearching

    SearchResult() {
    }

    SearchResult(String viewName, boolean keepSearching) {
        this.viewName = viewName
        this.keepSearching = keepSearching
    }

    String getViewName() {
        return viewName
    }

    void setViewName(String viewName) {
        this.viewName = viewName
    }

    boolean isKeepSearching() {
        return keepSearching
    }

    void setKeepSearching(boolean keepSearching) {
        this.keepSearching = keepSearching

    }

    void addEntry(String name, Object value) {
        entries.put(name, value)
    }

    Map<String, Object> getEntries() {
        return entries
    }

}
