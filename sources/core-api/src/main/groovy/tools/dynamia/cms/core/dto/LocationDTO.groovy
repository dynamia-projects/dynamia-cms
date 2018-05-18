package tools.dynamia.cms.core.dto

class LocationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L
    private String name
    private String code
    private String externalRef

    LocationDTO() {
		// TODO Auto-generated constructor stub
	}

    LocationDTO(String name, String code) {
		super()
        this.name = name
        this.code = code
    }

    String getExternalRef() {
		return externalRef
    }

    void setExternalRef(String externalRef) {
		this.externalRef = externalRef
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getCode() {
		return code
    }

    void setCode(String code) {
		this.code = code
    }

}
