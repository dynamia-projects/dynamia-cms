/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.mail.domain

import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
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
