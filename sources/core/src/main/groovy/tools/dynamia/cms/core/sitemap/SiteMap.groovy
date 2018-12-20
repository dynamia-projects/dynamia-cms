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

package tools.dynamia.cms.core.sitemap

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "urlset")

class SiteMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5296377121107137065L

    @XmlElement(name = "url")
	private List<SiteMapURL> urls = new ArrayList<>()

    List<SiteMapURL> getUrls() {
		if (urls == null) {
			urls = new ArrayList<>()
        }
		return urls
    }

    void setUrls(List<SiteMapURL> urls) {
		this.urls = urls
    }

    void addUrl(SiteMapURL url) {
		urls.add(url)
    }

}
