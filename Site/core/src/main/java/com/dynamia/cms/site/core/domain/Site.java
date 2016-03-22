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
package com.dynamia.cms.site.core.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.dynamia.cms.site.core.api.Parameterizable;

import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.contraints.NotEmpty;

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "cr_sites")
public class Site extends SimpleEntity implements Parameterizable {

	@NotEmpty
	private String name;
	@Column(name = "site_key", unique = true)
	@NotEmpty
	private String key;
	@OneToMany(mappedBy = "site")
	private List<SiteDomain> acceptedDomains = new ArrayList<>();

	@OneToMany(mappedBy = "site", fetch = FetchType.EAGER)
	private List<SiteParameter> parameters = new ArrayList<>();

	@OneToOne
	private Site parent;

	private String description;
	private boolean offline;
	private String offlineMessage;
	private String offlineIcon;
	private String template = "dynamical";
	private String metadataKeywords;
	private String metadataAuthor;
	private String metadataDescription;
	private String metadataRights;
	private String googleAnalyticsID;
	private String googleAnalyticsDomain;
	private String googleSiteVerification;

	public Site getParent() {
		return parent;
	}

	public void setParent(Site parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SiteDomain> getAcceptedDomains() {
		return acceptedDomains;
	}

	public void setAcceptedDomains(List<SiteDomain> acceptedDomains) {
		this.acceptedDomains = acceptedDomains;
	}

	@Override
	public List<SiteParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<SiteParameter> parameters) {
		this.parameters = parameters;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGoogleAnalyticsID() {
		return googleAnalyticsID;
	}

	public void setGoogleAnalyticsID(String googleAnalyticsID) {
		this.googleAnalyticsID = googleAnalyticsID;
	}

	public String getGoogleAnalyticsDomain() {
		return googleAnalyticsDomain;
	}

	public void setGoogleAnalyticsDomain(String googleAnalyticsDomain) {
		this.googleAnalyticsDomain = googleAnalyticsDomain;
	}

	public String getGoogleSiteVerification() {
		return googleSiteVerification;
	}

	public void setGoogleSiteVerification(String googleSiteVerification) {
		this.googleSiteVerification = googleSiteVerification;
	}

	public boolean isOffline() {
		return offline;
	}

	public void setOffline(boolean offline) {
		this.offline = offline;
	}

	public String getOfflineMessage() {
		return offlineMessage;
	}

	public void setOfflineMessage(String offlineMessage) {
		this.offlineMessage = offlineMessage;
	}

	public String getOfflineIcon() {
		return offlineIcon;
	}

	public void setOfflineIcon(String offlineIcon) {
		this.offlineIcon = offlineIcon;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getMetadataKeywords() {
		return metadataKeywords;
	}

	public void setMetadataKeywords(String metadataKeywords) {
		this.metadataKeywords = metadataKeywords;
	}

	public String getMetadataAuthor() {
		return metadataAuthor;
	}

	public void setMetadataAuthor(String metadataAuthor) {
		this.metadataAuthor = metadataAuthor;
	}

	public String getMetadataDescription() {
		return metadataDescription;
	}

	public void setMetadataDescription(String metadataDescription) {
		this.metadataDescription = metadataDescription;
	}

	public String getMetadataRights() {
		return metadataRights;
	}

	public void setMetadataRights(String metadataRights) {
		this.metadataRights = metadataRights;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", name, key);
	}

}
