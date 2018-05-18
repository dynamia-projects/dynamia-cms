package tools.dynamia.cms.core.dto

class SiteDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L
    private String name
    private String key
    private String domain

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getKey() {
		return key
    }

    void setKey(String key) {
		this.key = key
    }

    String getDomain() {
		return domain
    }

    void setDomain(String domain) {
		this.domain = domain
    }

}
