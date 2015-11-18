package com.dynamia.cms.admin.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.domain.PageCategory;
import com.dynamia.cms.site.pages.services.PageService;

import tools.dynamia.integration.sterotypes.Provider;
import tools.dynamia.io.VirtualFile;
import tools.dynamia.io.VirtualFileProvider;

@Provider
public class PageVirtualFileProvider implements VirtualFileProvider {

	@Autowired
	private PageService service;

	@Override
	public List<VirtualFile> getVirtualFiles() {
		List<VirtualFile> files = new ArrayList<>();
		Site site = SiteContext.get().getCurrent();
		if (site != null) {

			PageCategoryVirtualFile pagesVF = new PageCategoryVirtualFile("pages", site, service);

			List<PageCategory> categories = service.getPagesCategories(site);
			for (PageCategory pageCategory : categories) {
				pagesVF.addSubdirectory(new PageCategoryVirtualFile(service, pageCategory));
			}

			
			files.add(pagesVF);			
		} 
		
		return files;
	}

}
