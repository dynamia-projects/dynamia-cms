/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import com.dynamia.cms.site.core.controllers.CacheController;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.integration.Containers;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author mario_2
 */
@Component("siteContext")
@Scope("session")
public class SiteContext implements Serializable {

	@Autowired(required = false)
	private CacheController cacheController;
	private Site current;
	private String currentURI;
	private long lastCacheClear;

	public static SiteContext get() {
		return Containers.get().findObject(SiteContext.class);
	}

	public Site getCurrent() {
		checkCache();
		return current;
	}

	private void checkCache() {
		if (cacheController != null) {
			if (cacheController.getLastCacheClear() > lastCacheClear) {
				lastCacheClear = cacheController.getLastCacheClear();
				current = null;
			}
		}
	}

	public void setCurrent(Site current) {
		this.current = current;
	}

	public String getCurrentURI() {
		return currentURI;
	}

	public void setCurrentURI(String currentURI) {
		this.currentURI = currentURI;
	}

}
