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
	private static final long serialVersionUID = 1L
    @XmlElement(name = "loc")
	private String location
    @XmlElement(name = "lastmod")
	private Date lastModification
    @XmlElement(name = "changefreq")
	private SiteMapFrecuency changeFrequency
    @XmlElement
	private Float priority
    @XmlTransient
	private String name
    @XmlTransient
	private String description
    @XmlTransient
	private String category
    @XmlTransient
	private String imageURL
    @XmlTransient
	private String videoURL

    SiteMapURL() {
		// TODO Auto-generated constructor stub
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

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getDescription() {
		return description
    }

    void setDescription(String description) {
		this.description = description
    }

    String getCategory() {
		return category
    }

    void setCategory(String category) {
		this.category = category
    }

    String getImageURL() {
		return imageURL
    }

    void setImageURL(String imageURL) {
		this.imageURL = imageURL
    }

    String getVideoURL() {
		return videoURL
    }

    void setVideoURL(String videoURL) {
		this.videoURL = videoURL
    }

    String getLocation() {
		return location
    }

    void setLocation(String location) {
		this.location = location
    }

    Date getLastModification() {
		return lastModification
    }

    void setLastModification(Date lastModification) {
		this.lastModification = lastModification
    }

    SiteMapFrecuency getChangeFrequency() {
		return changeFrequency
    }

    void setChangeFrequency(SiteMapFrecuency changeFrequency) {
		this.changeFrequency = changeFrequency
    }

    float getPriority() {
		return priority
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
