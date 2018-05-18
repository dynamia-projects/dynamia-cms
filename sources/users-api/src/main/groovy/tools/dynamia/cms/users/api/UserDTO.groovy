package tools.dynamia.cms.users.api

class UserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 206617741224283708L
    private String email
    private String password
    private String fullName
    private String firstName
    private String lastName
    private String address
    private String phoneNumber
    private String mobileNumber
    private String city
    private String country
    private String identification
    private String externalRef
    private boolean enabled
    private String relatedUser
    private UserProfile profile = UserProfile.USER
    private String groupName

    String getGroupName() {
		return groupName
    }

    void setGroupName(String groupName) {
		this.groupName = groupName
    }

    String getFullName() {
		return fullName
    }

    void setFullName(String fullName) {
		this.fullName = fullName
    }

    UserProfile getProfile() {
		return profile
    }

    void setProfile(UserProfile profile) {
		this.profile = profile
    }

    String getRelatedUser() {
		return relatedUser
    }

    void setRelatedUser(String relatedUser) {
		this.relatedUser = relatedUser
    }

    String getCity() {
		return city
    }

    void setCity(String city) {
		this.city = city
    }

    String getCountry() {
		return country
    }

    void setCountry(String country) {
		this.country = country
    }

    String getEmail() {
		return email
    }

    void setEmail(String email) {
		this.email = email
    }

    String getPassword() {
		return password
    }

    void setPassword(String password) {
		this.password = password
    }

    String getFirstName() {
		return firstName
    }

    void setFirstName(String firstName) {
		this.firstName = firstName
    }

    String getLastName() {
		return lastName
    }

    void setLastName(String lastName) {
		this.lastName = lastName
    }

    String getAddress() {
		return address
    }

    void setAddress(String address) {
		this.address = address
    }

    String getPhoneNumber() {
		return phoneNumber
    }

    void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber
    }

    String getMobileNumber() {
		return mobileNumber
    }

    void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber
    }

    String getIdentification() {
		return identification
    }

    void setIdentification(String identification) {
		this.identification = identification
    }

    String getExternalRef() {
		return externalRef
    }

    void setExternalRef(String externalRef) {
		this.externalRef = externalRef
    }

    boolean isEnabled() {
		return enabled
    }

    void setEnabled(boolean enabled) {
		this.enabled = enabled
    }

	@Override
    String toString() {
		return "UserDTO [email=" + email + ", password=" + password + ", fullName=" + fullName + ", firstName="
				+ firstName + ", lastName=" + lastName + ", address=" + address + ", phoneNumber=" + phoneNumber
				+ ", mobileNumber=" + mobileNumber + ", city=" + city + ", country=" + country + ", identification="
				+ identification + ", externalRef=" + externalRef + ", enabled=" + enabled + ", relatedUser="
				+ relatedUser + ", profile=" + profile + ", groupName=" + groupName + "]"
    }

}
