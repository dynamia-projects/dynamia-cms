package tools.dynamia.cms.site.core.dto

public class LocationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String code;
	private String externalRef;

	public LocationDTO() {
		// TODO Auto-generated constructor stub
	}

	public LocationDTO(String name, String code) {
		super();
		this.name = name;
		this.code = code;
	}

	public String getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(String externalRef) {
		this.externalRef = externalRef;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
