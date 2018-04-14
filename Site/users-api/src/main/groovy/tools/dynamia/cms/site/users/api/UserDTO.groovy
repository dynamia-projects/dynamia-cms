package tools.dynamia.cms.site.users.api;

import java.io.Serializable;

public class UserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 206617741224283708L;
	private String email;
	private String password;
	private String fullName;
	private String firstName;
	private String lastName;
	private String address;
	private String phoneNumber;
	private String mobileNumber;
	private String city;
	private String country;
	private String identification;
	private String externalRef;
	private boolean enabled;
	private String relatedUser;
	private UserProfile profile = UserProfile.USER;
	private String groupName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	public String getRelatedUser() {
		return relatedUser;
	}

	public void setRelatedUser(String relatedUser) {
		this.relatedUser = relatedUser;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "UserDTO [email=" + email + ", password=" + password + ", fullName=" + fullName + ", firstName="
				+ firstName + ", lastName=" + lastName + ", address=" + address + ", phoneNumber=" + phoneNumber
				+ ", mobileNumber=" + mobileNumber + ", city=" + city + ", country=" + country + ", identification="
				+ identification + ", externalRef=" + externalRef + ", enabled=" + enabled + ", relatedUser="
				+ relatedUser + ", profile=" + profile + ", groupName=" + groupName + "]";
	}

}
