package tools.dynamia.cms.site.core.sitemap

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlTransient

@XmlAccessorType(XmlAccessType.FIELD)
public class SiteMapURL implements Serializable {

	public static float MINIMUM = 0.0f;
	public static float LOW = 0.3f;
	public static float MEDIUM = 0.5f;
	public static float HIGH = 1.0f;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "loc")
	private String location;
	@XmlElement(name = "lastmod")	
	private Date lastModification;
	@XmlElement(name = "changefreq")
	private SiteMapFrecuency changeFrequency;
	@XmlElement
	private Float priority;
	@XmlTransient
	private String name;
	@XmlTransient
	private String description;
	@XmlTransient
	private String category;
	@XmlTransient
	private String imageURL;
	@XmlTransient
	private String videoURL;

	public SiteMapURL() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param location
	 */
	public SiteMapURL(String location) {
		super();
		this.location = location;
	}

	/**
	 * 
	 * @param location
	 * @param lastModification
	 */
	public SiteMapURL(String location, Date lastModification) {
		super();
		this.location = location;
		this.lastModification = lastModification;
	}

	/**
	 * 
	 * @param location
	 * @param lastModification
	 * @param changeFrecuency
	 */
	public SiteMapURL(String location, Date lastModification, SiteMapFrecuency changeFrecuency) {
		super();
		this.location = location;
		this.lastModification = lastModification;
		this.changeFrequency = changeFrecuency;
	}

	/**
	 * 
	 * @param location
	 * @param lastModification
	 * @param changeFrecuency
	 * @param priority
	 */
	public SiteMapURL(String location, Date lastModification, SiteMapFrecuency changeFrecuency, float priority) {
		super();
		this.location = location;
		this.lastModification = lastModification;
		this.changeFrequency = changeFrecuency;
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getVideoURL() {
		return videoURL;
	}

	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getLastModification() {
		return lastModification;
	}

	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}

	public SiteMapFrecuency getChangeFrequency() {
		return changeFrequency;
	}

	public void setChangeFrequency(SiteMapFrecuency changeFrequency) {
		this.changeFrequency = changeFrequency;
	}

	public float getPriority() {
		return priority;
	}

	/**
	 * From 0.0 to 1.0
	 * 
	 * @param priority
	 */
	public void setPriority(float priority) {
		this.priority = priority;
		if (priority > HIGH || priority < MINIMUM) {
			priority = MEDIUM;
		}
	}

}
