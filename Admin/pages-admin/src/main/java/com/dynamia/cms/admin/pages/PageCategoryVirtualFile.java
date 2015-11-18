package com.dynamia.cms.admin.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.pages.domain.PageCategory;
import com.dynamia.cms.site.pages.services.PageService;

import tools.dynamia.io.VirtualFile;

public class PageCategoryVirtualFile extends VirtualFile {

	private PageCategory pageCategory;
	private PageService pageService;
	private Site site;
	private List<PageCategoryVirtualFile> subdirectories = new ArrayList<>();

	public PageCategoryVirtualFile(String name, Site site, PageService pageService) {
		super(name);
		setName(name);
		this.pageService = pageService;
		this.site = site;
		init();
	}

	public PageCategoryVirtualFile(PageService pageService, PageCategory pageCategory) {
		super(pageCategory.getName());
		setName(pageCategory.getName());
		this.pageCategory = pageCategory;
		this.pageService = pageService;
		init();
	}

	private void init() {
		setDirectory(true);
		setCanWrite(false);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4309978768150061535L;

	@Override
	public List<File> getChildren() {
		List<Page> pages = Collections.EMPTY_LIST;
		if (pageCategory != null) {
			pages = pageService.getPages(pageCategory.getSite(), pageCategory);
		} else if (site != null) {
			pages = pageService.getPagesWithoutCategory(site);
		}

		List<File> children = new ArrayList<>();
		children.addAll(subdirectories);
		children.addAll(pages.stream()
				.map(p -> new PageVirtualFile(p))
				.collect(Collectors.toList()));

		return children;
	}

	public void addSubdirectory(PageCategoryVirtualFile directory) {
		subdirectories.add(directory);
	}

	public List<PageCategoryVirtualFile> getSubdirectories() {
		return subdirectories;
	}

}
