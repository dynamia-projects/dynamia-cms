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
package tools.dynamia.cms.site.mail.domain

import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "ntf_email_accounts")
class MailAccount extends SimpleEntity implements SiteAware {

	@OneToOne
	@NotNull
	private Site site

    @NotEmpty(message = "Enter account's name")
	private String name
    @NotEmpty(message = "Enter account's username")
	private String username
    private String password
    @NotEmpty(message = "Enter server host name or ip address")
	private String serverAddress
    private String fromAddress
    private int port = 25
    private boolean useTTLS
    private boolean loginRequired
    private boolean preferred
    private String enconding
    private long timestamp

    long getTimestamp() {
		return timestamp
    }

    void setTimestamp(long timestamp) {
		this.timestamp = timestamp
    }

    Site getSite() {
		return site
    }

    void setSite(Site site) {
		this.site = site
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getUsername() {
		return username
    }

    void setUsername(String username) {
		this.username = username
    }

    String getPassword() {
		return password
    }

    void setPassword(String password) {
		this.password = password
    }

    String getServerAddress() {
		return serverAddress
    }

    void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress
    }

    String getFromAddress() {
		return fromAddress
    }

    void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress
    }

    int getPort() {
		return port
    }

    void setPort(int port) {
		this.port = port
    }

    boolean isUseTTLS() {
		return useTTLS
    }

    void setUseTTLS(boolean useTTLS) {
		this.useTTLS = useTTLS
    }

    boolean isLoginRequired() {
		return loginRequired
    }

    void setLoginRequired(boolean loginRequired) {
		this.loginRequired = loginRequired
    }

    boolean isPreferred() {
		return preferred
    }

    void setPreferred(boolean preferred) {
		this.preferred = preferred
    }

    String getEnconding() {
		return enconding
    }

    void setEnconding(String enconding) {
		this.enconding = enconding
    }

	@Override
    String toString() {
		return String.format("%s (%s)", name, fromAddress)
    }

}
