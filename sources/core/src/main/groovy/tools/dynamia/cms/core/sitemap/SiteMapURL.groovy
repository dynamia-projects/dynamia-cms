/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
