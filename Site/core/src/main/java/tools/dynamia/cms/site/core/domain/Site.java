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
package tools.dynamia.cms.site.core.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tools.dynamia.cms.site.core.api.Parameterizable;

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
	@JsonIgnore
	private List<SiteDomain> acceptedDomains = new ArrayList<>();

	@OneToMany(mappedBy = "site", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<SiteParameter> parameters = new ArrayList<>();

	@OneToOne
	@JsonIgnore
	private Site parent;

	private String description;
	private boolean offline;
	private String offlineMessage;
	private String offlineIcon;
	private String offlineRedirect;
	private String template = "dynamical";
	private String metadataKeywords;
	private String metadataAuthor;
	private String metadataDescription;
	private String metadataRights;
	private String googleAnalyticsID;
	private String googleAnalyticsDomain;
	private String googleSiteVerification;
	private boolean corporateSite;

	private String twitter;
	private String googlePlus;
	private String facebook;
	private String instagram;
	private String linkedin;
	private String tumblr;
	private String snapchat;
	private String pinterest;
	private String vk;
	private String flickr;
	private String vine;
	private String meetup;

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getGooglePlus() {
		return googlePlus;
	}

	public void setGooglePlus(String googlePlus) {
		this.googlePlus = googlePlus;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

	public String getTumblr() {
		return tumblr;
	}

	public void setTumblr(String tumblr) {
		this.tumblr = tumblr;
	}

	public String getSnapchat() {
		return snapchat;
	}

	public void setSnapchat(String snapchat) {
		this.snapchat = snapchat;
	}

	public String getPinterest() {
		return pinterest;
	}

	public void setPinterest(String pinterest) {
		this.pinterest = pinterest;
	}

	public String getVk() {
		return vk;
	}

	public void setVk(String vk) {
		this.vk = vk;
	}

	public String getFlickr() {
		return flickr;
	}

	public void setFlickr(String flickr) {
		this.flickr = flickr;
	}

	public String getVine() {
		return vine;
	}

	public void setVine(String vine) {
		this.vine = vine;
	}

	public String getMeetup() {
		return meetup;
	}

	public void setMeetup(String meetup) {
		this.meetup = meetup;
	}

	public String getOfflineRedirect() {
		return offlineRedirect;
	}

	public void setOfflineRedirect(String offlineRedirect) {
		this.offlineRedirect = offlineRedirect;
	}

	public boolean isCorporateSite() {
		return corporateSite;
	}

	public void setCorporateSite(boolean corporateSite) {
		this.corporateSite = corporateSite;
	}

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

	public String getParameterValue(String name) {
		for (SiteParameter siteParameter : parameters) {
			if (siteParameter.getName().equals(name)) {
				return siteParameter.getValue();
			}
		}
		return null;
	}

	public boolean isParameter(String name) {
		String value = getParameterValue(name);
		return value == null ? false : Boolean.valueOf(value);
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", name, key);
	}

}
