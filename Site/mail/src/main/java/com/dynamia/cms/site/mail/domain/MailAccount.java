/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.mail.domain;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.domain.SimpleEntity;
import com.dynamia.tools.domain.contraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "ntf_email_accounts")
public class MailAccount extends SimpleEntity implements SiteAware {

	@OneToOne
	@NotNull
	private Site site;

	@NotEmpty(message = "Enter account's name")
	private String name;
	@NotEmpty(message = "Enter account's username")
	private String username;
	private String password;
	@NotEmpty(message = "Enter server host name or ip address")
	private String serverAddress;
	private String fromAddress;
	private int port = 25;
	private boolean useTTLS;
	private boolean loginRequired;
	private boolean preferred;
	private String enconding;

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isUseTTLS() {
		return useTTLS;
	}

	public void setUseTTLS(boolean useTTLS) {
		this.useTTLS = useTTLS;
	}

	public boolean isLoginRequired() {
		return loginRequired;
	}

	public void setLoginRequired(boolean loginRequired) {
		this.loginRequired = loginRequired;
	}

	public boolean isPreferred() {
		return preferred;
	}

	public void setPreferred(boolean preferred) {
		this.preferred = preferred;
	}

	public String getEnconding() {
		return enconding;
	}

	public void setEnconding(String enconding) {
		this.enconding = enconding;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", name, fromAddress);
	}

}
