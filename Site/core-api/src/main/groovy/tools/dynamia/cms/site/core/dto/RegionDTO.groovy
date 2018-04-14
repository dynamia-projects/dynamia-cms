package tools.dynamia.cms.site.core.dto;

public class RegionDTO extends LocationDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CountryDTO country;
	private String taxName;
	private double taxPercent;

	public RegionDTO() {

	}

	public RegionDTO(String name, String code, CountryDTO country) {
		super(name, code);
		this.country = country;
	}

	public CountryDTO getCountry() {
		return country;
	}

	public void setCountry(CountryDTO country) {
		this.country = country;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public double getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(double taxPercent) {
		this.taxPercent = taxPercent;
	}

}
