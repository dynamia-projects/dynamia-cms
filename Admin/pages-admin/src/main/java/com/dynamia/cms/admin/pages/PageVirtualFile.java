package com.dynamia.cms.admin.pages;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.api.URLProvider;
import com.dynamia.cms.site.pages.domain.Page;

import tools.dynamia.io.VirtualFile;

public class PageVirtualFile extends VirtualFile implements URLProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Page page;

	public PageVirtualFile(Page page) {
		super(page.getAlias());
		this.page = page;
		setName(page.getTitle()+".html");
		setCanWrite(false);
		setDirectory(false);

	}

	@Override
	public String getURL() {
		return CMSUtil.getSiteURL(page.getSite(), page.getAlias());
	}

	@Override
	public boolean isFile() {
		return true;
	}
	
	@Override
	public long lastModified() {
		return page.getCreationDate().getTime();
	}
}
