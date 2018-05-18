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
