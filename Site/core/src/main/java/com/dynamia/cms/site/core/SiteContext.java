/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.integration.Containers;

/**
 *
 * @author mario_2
 */
@Component("siteContext")
@Scope("session")
public class SiteContext implements Serializable {

	
	@Autowired
	private SiteService service;

	private Site current;
	private String siteURL;
	private String currentURI;
	private String currentURL;
	private String previousURI;
	private String previousURL;
	

	public static SiteContext get() {
		return Containers.get().findObject(SiteContext.class);
	}

	public Site getCurrent() {
		
		return current;
	}

	

	public void setCurrent(Site current) {
		this.current = current;
	}

	public String getCurrentURI() {
		return currentURI;
	}

	void setCurrentURI(String currentURI) {
		this.previousURI = this.currentURI;
		this.currentURI = currentURI;
	}

	public String getCurrentURL() {
		return currentURL;
	}

	void setCurrentURL(String currentURL) {
		this.previousURL = this.currentURL;
		this.currentURL = currentURL;
	}

	public String getPreviousURI() {
		return previousURI;
	}

	public String getPreviousURL() {
		return previousURL;
	}

	public String getSiteURL() {
		return siteURL;
	}

	void setSiteURL(String siteURL) {
		this.siteURL = siteURL;
	}

}
