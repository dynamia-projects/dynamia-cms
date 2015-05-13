/**
 * Dynamia Soluciones IT Todos los Derechos Reservados (c) Colombia
 *
 * Prohibida la reproducción parcial o total de este archivo de código fuente en
 * proyectos de software NO realizados por Dynamia Soluciones IT. Cualquier otro
 * archivo de código fuente o librería que haga referencia a este tendrá la
 * misma licencia.
 *
 * mas info: http://www.dynamiasoluciones.com/licencia.html
 */
package com.dynamia.cms.site.users.domain;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.commons.StringUtils;
import com.dynamia.tools.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dynamia.tools.domain.ValidationError;
import com.dynamia.tools.domain.contraints.Email;
import com.dynamia.tools.domain.contraints.NotEmpty;
import com.dynamia.tools.domain.util.ContactInfo;

import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;

/**
 * Archivo: Usuario.java Fecha de Creacion: 27/06/2009
 *
 * @author Ing. Mario Serrano Leones
 */
@Entity
@Table(name = "usr_users", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "site_id", "username" })
})
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
	private boolean superadmin;

	@NotNull(message = "Enter user full name")
	@NotEmpty
	private String fullName;

	private String firstName;
	private String lastName;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<UserProfile> profiles = new ArrayList<>();

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

	public boolean isSuperadmin() {
		return superadmin;
	}

	public void setSuperadmin(boolean superadmin) {
		this.superadmin = superadmin;
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

	public List<UserProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<UserProfile> profiles) {
		this.profiles = profiles;
	}

	public boolean isPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

	public void addProfile(Profile profile) {
		if (profile != null) {
			for (UserProfile p : profiles) {
				if (p.getProfile().equals(profile)) {
					throw new ValidationError("Profile " + profile.getName() + " already assigned");
				}
			}
			profiles.add(new UserProfile(profile, this));
		}
	}

	public void removeProfile(UserProfile profile) {
		if (profile != null) {
			profiles.remove(profile);
			profile.setProfile(null);
			profile.setUser(this);
		}
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
		for (int i = 0; i < profiles.size(); i++) {
			UserProfile perfilUsuario = profiles.get(i);
			auths.add(perfilUsuario.getProfile());
		}
		return auths;
	}

	public Profile getProfile(String profileName) {
		if (getAuthorities() != null) {
			for (GrantedAuthority grantedAuthority : getAuthorities()) {
				if (grantedAuthority.getAuthority().equals(profileName)) {
					return (Profile) grantedAuthority;
				}
			}
		}
		return null;
	}

	public String getAssignedProfiles() {
		StringBuilder sb = new StringBuilder();
		for (UserProfile profile : profiles) {
			sb.append(profile.getProfile().getName()).append(", ");
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.lastIndexOf(", "));
		}
		return sb.toString();
	}

	public static User createMock() {
		User user = new User();
		user.setUsername("Anonymous");
		user.setFullName("Anonymous");
		return user;
	}

	public boolean hasProfile(String string) {
		return getProfile(string) != null;
	}
}
