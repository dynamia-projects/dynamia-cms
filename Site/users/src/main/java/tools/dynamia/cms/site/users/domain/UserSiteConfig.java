package tools.dynamia.cms.site.users.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import tools.dynamia.cms.site.core.domain.SiteSimpleEntity;

@Entity
@Table(name = "usr_config")
public class UserSiteConfig extends SiteSimpleEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean registrationEnabled;
	private boolean synchronizationEnabled;
	private String datasourceURL;
	private String datasourceUsername;
	private String datasourcePassword;

	public boolean isRegistrationEnabled() {
		return registrationEnabled;
	}

	public void setRegistrationEnabled(boolean registrationEnabled) {
		this.registrationEnabled = registrationEnabled;
	}

	public boolean isSynchronizationEnabled() {
		return synchronizationEnabled;
	}

	public void setSynchronizationEnabled(boolean synchronizationEnabled) {
		this.synchronizationEnabled = synchronizationEnabled;
	}

	public String getDatasourceURL() {
		return datasourceURL;
	}

	public void setDatasourceURL(String datasourceURL) {
		this.datasourceURL = datasourceURL;
	}

	public String getDatasourceUsername() {
		return datasourceUsername;
	}

	public void setDatasourceUsername(String datasourceUsername) {
		this.datasourceUsername = datasourceUsername;
	}

	public String getDatasourcePassword() {
		return datasourcePassword;
	}

	public void setDatasourcePassword(String datasourcePassword) {
		this.datasourcePassword = datasourcePassword;
	}

}
