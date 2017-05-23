package tools.dynamia.cms.site.users.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import tools.dynamia.cms.site.core.domain.SiteSimpleEntity;
import tools.dynamia.cms.site.mail.domain.MailTemplate;

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
	private boolean registrationValidated;

	@Column(length = 3000)
	private String registrationPendingMessage;
	@Column(length = 3000)
	private String registrationCompletedMessage;

	@OneToOne
	private MailTemplate registrationPendingTemplate;

	@OneToOne
	private MailTemplate registrationCompletedTemplate;

	private String auxFieldName1;
	private String auxFieldName2;
	private String auxFieldName3;
	private String auxFieldName4;
	private String auxFieldName5;
	private String auxFieldName6;
	private String auxFieldName7;
	private String auxFieldName8;
	private String auxFieldName9;
	private String auxFieldName10;
	private String auxFieldName11;
	private String auxFieldName12;
	private String auxFieldName13;
	private String auxFieldName14;
	private String auxFieldName15;

	public String getRegistrationPendingMessage() {
		return registrationPendingMessage;
	}

	public void setRegistrationPendingMessage(String registrationPendingMessage) {
		this.registrationPendingMessage = registrationPendingMessage;
	}

	public String getRegistrationCompletedMessage() {
		return registrationCompletedMessage;
	}

	public void setRegistrationCompletedMessage(String registrationCompletedMessage) {
		this.registrationCompletedMessage = registrationCompletedMessage;
	}

	public MailTemplate getRegistrationPendingTemplate() {
		return registrationPendingTemplate;
	}

	public void setRegistrationPendingTemplate(MailTemplate registrationPendingTemplate) {
		this.registrationPendingTemplate = registrationPendingTemplate;
	}

	public MailTemplate getRegistrationCompletedTemplate() {
		return registrationCompletedTemplate;
	}

	public void setRegistrationCompletedTemplate(MailTemplate registrationCompletedTemplate) {
		this.registrationCompletedTemplate = registrationCompletedTemplate;
	}

	public boolean isRegistrationValidated() {
		return registrationValidated;
	}

	public void setRegistrationValidated(boolean registrationValidated) {
		this.registrationValidated = registrationValidated;
	}

	public String getAuxFieldName1() {
		return auxFieldName1;
	}

	public void setAuxFieldName1(String auxFieldName1) {
		this.auxFieldName1 = auxFieldName1;
	}

	public String getAuxFieldName2() {
		return auxFieldName2;
	}

	public void setAuxFieldName2(String auxFieldName2) {
		this.auxFieldName2 = auxFieldName2;
	}

	public String getAuxFieldName3() {
		return auxFieldName3;
	}

	public void setAuxFieldName3(String auxFieldName3) {
		this.auxFieldName3 = auxFieldName3;
	}

	public String getAuxFieldName4() {
		return auxFieldName4;
	}

	public void setAuxFieldName4(String auxFieldName4) {
		this.auxFieldName4 = auxFieldName4;
	}

	public String getAuxFieldName5() {
		return auxFieldName5;
	}

	public void setAuxFieldName5(String auxFieldName5) {
		this.auxFieldName5 = auxFieldName5;
	}

	public String getAuxFieldName6() {
		return auxFieldName6;
	}

	public void setAuxFieldName6(String auxFieldName6) {
		this.auxFieldName6 = auxFieldName6;
	}

	public String getAuxFieldName7() {
		return auxFieldName7;
	}

	public void setAuxFieldName7(String auxFieldName7) {
		this.auxFieldName7 = auxFieldName7;
	}

	public String getAuxFieldName8() {
		return auxFieldName8;
	}

	public void setAuxFieldName8(String auxFieldName8) {
		this.auxFieldName8 = auxFieldName8;
	}

	public String getAuxFieldName9() {
		return auxFieldName9;
	}

	public void setAuxFieldName9(String auxFieldName9) {
		this.auxFieldName9 = auxFieldName9;
	}

	public String getAuxFieldName10() {
		return auxFieldName10;
	}

	public void setAuxFieldName10(String auxFieldName10) {
		this.auxFieldName10 = auxFieldName10;
	}

	public String getAuxFieldName11() {
		return auxFieldName11;
	}

	public void setAuxFieldName11(String auxFieldName11) {
		this.auxFieldName11 = auxFieldName11;
	}

	public String getAuxFieldName12() {
		return auxFieldName12;
	}

	public void setAuxFieldName12(String auxFieldName12) {
		this.auxFieldName12 = auxFieldName12;
	}

	public String getAuxFieldName13() {
		return auxFieldName13;
	}

	public void setAuxFieldName13(String auxFieldName13) {
		this.auxFieldName13 = auxFieldName13;
	}

	public String getAuxFieldName14() {
		return auxFieldName14;
	}

	public void setAuxFieldName14(String auxFieldName14) {
		this.auxFieldName14 = auxFieldName14;
	}

	public String getAuxFieldName15() {
		return auxFieldName15;
	}

	public void setAuxFieldName15(String auxFieldName15) {
		this.auxFieldName15 = auxFieldName15;
	}

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
