package tools.dynamia.cms.site.products.dto

public class ProductsReviewResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1234515750141844185L;
	private boolean accepted;
	private List<ProductDTO> products = new ArrayList<>();

	private String email;
	private String name;
	private String lastName;
	private String identification;
	private String externalRef;
	private String address;
	private String phoneNumber;
	private String mobileNumber;
	private String city;
	private String region;
	private String country;
	private String description;
	private Date date;
	private String document;

	public ProductsReviewResponse() {

	}

	public ProductsReviewResponse(List<ProductDTO> products, String email, String name) {
		super();
		this.products = products;
		this.email = email;
		this.name = name;
		this.accepted = true;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public List<ProductDTO> getProducts() {
		return products;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(String externalRef) {
		this.externalRef = externalRef;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public static ProductsReviewResponse rejected() {
		return new ProductsReviewResponse();
	}

	public static ProductsReviewResponse rejected(String description) {
		return new ProductsReviewResponse();
	}
}
