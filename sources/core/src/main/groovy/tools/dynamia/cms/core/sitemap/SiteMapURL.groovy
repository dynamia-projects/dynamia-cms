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
import javax.xml.bind.annotation.XmlTransient

@XmlAccessorType(XmlAccessType.FIELD)
class SiteMapURL implements Serializable {

    public static float MINIMUM = 0.0f
    public static float LOW = 0.3f
    public static float MEDIUM = 0.5f
    public static float HIGH = 1.0f

    /**
     *
     */
    static final long serialVersionUID = 1L
    @XmlElement(name = "loc")
    String location
    @XmlElement(name = "lastmod")
    Date lastModification
    @XmlElement(name = "changefreq")
    SiteMapFrecuency changeFrequency
    @XmlElement
    Float priority
    @XmlTransient
    String name
    @XmlTransient
    String description
    @XmlTransient
    String category
    @XmlTransient
    String imageURL
    @XmlTransient
    String videoURL

    SiteMapURL() {
        // Default
    }

    /**
     *
     * @param location
     */
    SiteMapURL(String location) {
        super()
        this.location = location
    }

    /**
     *
     * @param location
     * @param lastModification
     */
    SiteMapURL(String location, Date lastModification) {
        super()
        this.location = location
        this.lastModification = lastModification
    }

    /**
     *
     * @param location
     * @param lastModification
     * @param changeFrecuency
     */
    SiteMapURL(String location, Date lastModification, SiteMapFrecuency changeFrecuency) {
        super()
        this.location = location
        this.lastModification = lastModification
        this.changeFrequency = changeFrecuency
    }

    /**
     *
     * @param location
     * @param lastModification
     * @param changeFrecuency
     * @param priority
     */
    SiteMapURL(String location, Date lastModification, SiteMapFrecuency changeFrecuency, float priority) {
        super()
        this.location = location
        this.lastModification = lastModification
        this.changeFrequency = changeFrecuency
        this.priority = priority
    }

    SiteMapURL name(String name) {
        this.name = name
        return this
    }


    SiteMapURL description(String description) {
        this.description = description
        return this
    }



    SiteMapURL category(String category) {
        this.category = category
        return this
    }


    SiteMapURL imageURL(String imageURL) {
        this.imageURL = imageURL
        return this
    }  

    SiteMapURL videoURL(String videoURL) {
        this.videoURL = videoURL
        return this
    }

    /**
     * From 0.0 to 1.0
     *
     * @param priority
     */
    void setPriority(float priority) {
        this.priority = priority
        if (priority > HIGH || priority < MINIMUM) {
            priority = MEDIUM
        }
    }

}
