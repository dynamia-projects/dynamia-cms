/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package tools.dynamia.cms.users.domain

import tools.dynamia.cms.core.domain.SiteSimpleEntity
import tools.dynamia.cms.mail.domain.MailAccount
import tools.dynamia.cms.mail.domain.MailTemplate

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "usr_config")
class UserSiteConfig extends SiteSimpleEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L
    private boolean registrationEnabled
    private boolean synchronizationEnabled
    private String datasourceURL
    private String datasourceUsername
    private String datasourcePassword
    private boolean registrationValidated

    @Column(length = 3000)
	private String registrationPendingMessage
    @Column(length = 3000)
	private String registrationCompletedMessage

    @OneToOne
	private MailTemplate registrationPendingTemplate

    @OneToOne
	private MailTemplate registrationCompletedTemplate

    @OneToOne
	private MailTemplate emailValidationTemplate

    @OneToOne
	private MailAccount mailAccount

    private boolean requireEmailActivation

    private String auxFieldName1
    private String auxFieldName2
    private String auxFieldName3
    private String auxFieldName4
    private String auxFieldName5
    private String auxFieldName6
    private String auxFieldName7
    private String auxFieldName8
    private String auxFieldName9
    private String auxFieldName10
    private String auxFieldName11
    private String auxFieldName12
    private String auxFieldName13
    private String auxFieldName14
    private String auxFieldName15

    String getRegistrationPendingMessage() {
		return registrationPendingMessage
    }

    void setRegistrationPendingMessage(String registrationPendingMessage) {
		this.registrationPendingMessage = registrationPendingMessage
    }

    String getRegistrationCompletedMessage() {
		return registrationCompletedMessage
    }

    void setRegistrationCompletedMessage(String registrationCompletedMessage) {
		this.registrationCompletedMessage = registrationCompletedMessage
    }

    MailTemplate getRegistrationPendingTemplate() {
		return registrationPendingTemplate
    }

    void setRegistrationPendingTemplate(MailTemplate registrationPendingTemplate) {
		this.registrationPendingTemplate = registrationPendingTemplate
    }

    MailTemplate getRegistrationCompletedTemplate() {
		return registrationCompletedTemplate
    }

    void setRegistrationCompletedTemplate(MailTemplate registrationCompletedTemplate) {
		this.registrationCompletedTemplate = registrationCompletedTemplate
    }

    boolean isRegistrationValidated() {
		return registrationValidated
    }

    void setRegistrationValidated(boolean registrationValidated) {
		this.registrationValidated = registrationValidated
    }

    String getAuxFieldName1() {
		return auxFieldName1
    }

    void setAuxFieldName1(String auxFieldName1) {
		this.auxFieldName1 = auxFieldName1
    }

    String getAuxFieldName2() {
		return auxFieldName2
    }

    void setAuxFieldName2(String auxFieldName2) {
		this.auxFieldName2 = auxFieldName2
    }

    String getAuxFieldName3() {
		return auxFieldName3
    }

    void setAuxFieldName3(String auxFieldName3) {
		this.auxFieldName3 = auxFieldName3
    }

    String getAuxFieldName4() {
		return auxFieldName4
    }

    void setAuxFieldName4(String auxFieldName4) {
		this.auxFieldName4 = auxFieldName4
    }

    String getAuxFieldName5() {
		return auxFieldName5
    }

    void setAuxFieldName5(String auxFieldName5) {
		this.auxFieldName5 = auxFieldName5
    }

    String getAuxFieldName6() {
		return auxFieldName6
    }

    void setAuxFieldName6(String auxFieldName6) {
		this.auxFieldName6 = auxFieldName6
    }

    String getAuxFieldName7() {
		return auxFieldName7
    }

    void setAuxFieldName7(String auxFieldName7) {
		this.auxFieldName7 = auxFieldName7
    }

    String getAuxFieldName8() {
		return auxFieldName8
    }

    void setAuxFieldName8(String auxFieldName8) {
		this.auxFieldName8 = auxFieldName8
    }

    String getAuxFieldName9() {
		return auxFieldName9
    }

    void setAuxFieldName9(String auxFieldName9) {
		this.auxFieldName9 = auxFieldName9
    }

    String getAuxFieldName10() {
		return auxFieldName10
    }

    void setAuxFieldName10(String auxFieldName10) {
		this.auxFieldName10 = auxFieldName10
    }

    String getAuxFieldName11() {
		return auxFieldName11
    }

    void setAuxFieldName11(String auxFieldName11) {
		this.auxFieldName11 = auxFieldName11
    }

    String getAuxFieldName12() {
		return auxFieldName12
    }

    void setAuxFieldName12(String auxFieldName12) {
		this.auxFieldName12 = auxFieldName12
    }

    String getAuxFieldName13() {
		return auxFieldName13
    }

    void setAuxFieldName13(String auxFieldName13) {
		this.auxFieldName13 = auxFieldName13
    }

    String getAuxFieldName14() {
		return auxFieldName14
    }

    void setAuxFieldName14(String auxFieldName14) {
		this.auxFieldName14 = auxFieldName14
    }

    String getAuxFieldName15() {
		return auxFieldName15
    }

    void setAuxFieldName15(String auxFieldName15) {
		this.auxFieldName15 = auxFieldName15
    }

    boolean isRegistrationEnabled() {
		return registrationEnabled
    }

    void setRegistrationEnabled(boolean registrationEnabled) {
		this.registrationEnabled = registrationEnabled
    }

    boolean isSynchronizationEnabled() {
		return synchronizationEnabled
    }

    void setSynchronizationEnabled(boolean synchronizationEnabled) {
		this.synchronizationEnabled = synchronizationEnabled
    }

    String getDatasourceURL() {
		return datasourceURL
    }

    void setDatasourceURL(String datasourceURL) {
		this.datasourceURL = datasourceURL
    }

    String getDatasourceUsername() {
		return datasourceUsername
    }

    void setDatasourceUsername(String datasourceUsername) {
		this.datasourceUsername = datasourceUsername
    }

    String getDatasourcePassword() {
		return datasourcePassword
    }

    void setDatasourcePassword(String datasourcePassword) {
		this.datasourcePassword = datasourcePassword
    }

    boolean isRequireEmailActivation() {
		return requireEmailActivation
    }

    void setRequireEmailActivation(boolean requireEmailActivation) {
		this.requireEmailActivation = requireEmailActivation
    }

    MailTemplate getEmailValidationTemplate() {
		return emailValidationTemplate
    }

    void setEmailValidationTemplate(MailTemplate emailValidationTemplate) {
		this.emailValidationTemplate = emailValidationTemplate
    }

    MailAccount getMailAccount() {
		return mailAccount
    }

    void setMailAccount(MailAccount mailAccount) {
		this.mailAccount = mailAccount
    }
}
