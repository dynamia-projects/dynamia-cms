/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.users.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.ContentAuthor;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.users.api.UserDTO;
import tools.dynamia.cms.site.users.api.UserProfile;
import tools.dynamia.commons.StringUtils;
import tools.dynamia.domain.BaseEntity;
import tools.dynamia.domain.contraints.Email;
import tools.dynamia.domain.contraints.NotEmpty;
import tools.dynamia.domain.util.ContactInfo;

/**
 * User entity
 *
 * @author Ing. Mario Serrano Leones
 */
@Entity
@Table(name = "usr_users", uniqueConstraints = { @UniqueConstraint(columnNames = { "site_id", "username" }) })
public class User extends BaseEntity implements UserDetails, SiteAware {

	@OneToOne
	@NotNull
	private Site site;

	@Column(updatable = false)
	@Size(min = 5, message = "El nombre de usuario debe ser minimo de 5 caracteres")
	@Email(message = "Ingrese direccion valida de email")
	private String username;
	@Size(min = 5, message = "El password del usuario debe ser minimo de 5 caracteres")
	private String password;

	private ContactInfo contactInfo = new ContactInfo();
	private String userphoto;
	private String identification;

	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	private boolean passwordExpired;

	@NotNull(message = "Enter user full name")
	@NotEmpty
	private String fullName;

	private String firstName;
	private String lastName;
	private String groupName;

	@Enumerated(EnumType.ORDINAL)
	private UserProfile profile = UserProfile.USER;

	@OneToOne
	private ContentAuthor relatedAuthor;

	private String externalRef;

	@OneToOne
	private User relatedUser;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public User getRelatedUser() {
		return relatedUser;
	}

	public void setRelatedUser(User relatedUser) {
		this.relatedUser = relatedUser;
	}

	public String getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(String externalRef) {
		this.externalRef = externalRef;
	}

	public ContentAuthor getRelatedAuthor() {
		return relatedAuthor;
	}

	public void setRelatedAuthor(ContentAuthor relatedAuthor) {
		this.relatedAuthor = relatedAuthor;
	}

	public UserProfile getProfile() {
		if (profile == null) {
			profile = UserProfile.USER;
		}
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	public String getFirstName() {
		if (firstName == null) {
			firstName = "";
		}
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		getFullName();
	}

	public String getLastName() {
		if (lastName == null) {
			lastName = "";
		}
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		getFullName();
	}

	public String getUserphoto() {
		return userphoto;
	}

	public void setUserphoto(String userphoto) {
		this.userphoto = userphoto;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void setSite(Site site) {
		this.site = site;
	}

	public ContactInfo getContactInfo() {
		if (contactInfo == null) {
			contactInfo = new ContactInfo();
		}
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getFullName() {

		fullName = getFirstName() + " " + getLastName();
		if (fullName != null) {
			fullName = StringUtils.toUpperCase(fullName);
		}

		if (fullName.equals("NULL NULL")) {
			fullName = "";
		}

		return fullName;
	}

	public void setFullName(String fullName) {

		this.fullName = fullName;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

	@Override
	public String toString() {
		if (username == null) {
			return super.toString();
		} else {
			return username;
		}
	}

	/*--------------------------------------------------*/
	@Override
	public List<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();

		auths.add(new SimpleGrantedAuthority("ROLE_" + UserProfile.USER.name()));
		if (getProfile() != UserProfile.USER) {
			auths.add(new SimpleGrantedAuthority("ROLE_" + getProfile().name()));
		}

		return auths;
	}

	public static User createMock() {
		User user = new User();
		user.setUsername("Anonymous");
		user.setFullName("Anonymous");
		return user;
	}

	public void sync(UserDTO dto) {
		if (dto.getProfile() != null) {
			profile = dto.getProfile();
		}

		username = dto.getEmail();
		externalRef = dto.getExternalRef();
		enabled = dto.isEnabled();
		firstName = dto.getFirstName();
		lastName = dto.getLastName();
		fullName = dto.getFullName();
		if (firstName == null) {
			firstName = fullName;
			lastName = "";
		}
		identification = dto.getIdentification();
		groupName = dto.getGroupName();
		if (contactInfo == null) {
			contactInfo = new ContactInfo();
		}
		contactInfo.setAddress(dto.getAddress());
		contactInfo.setEmail(dto.getEmail());
		contactInfo.setPhoneNumber(dto.getPhoneNumber());
		contactInfo.setMobileNumber(dto.getMobileNumber());
		contactInfo.setCity(dto.getCity());
		contactInfo.setCountry(dto.getCountry());
	}

}
