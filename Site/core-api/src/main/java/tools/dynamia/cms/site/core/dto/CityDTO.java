package tools.dynamia.cms.site.core.dto;

public class CityDTO extends LocationDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RegionDTO region;

	public CityDTO() {
		// TODO Auto-generated constructor stub
	}

	public CityDTO(String name, String code, RegionDTO region) {
		super(name, code);
		this.region = region;
	}

	public RegionDTO getRegion() {
		return region;
	}

	public void setRegion(RegionDTO region) {
		this.region = region;
	}

}
