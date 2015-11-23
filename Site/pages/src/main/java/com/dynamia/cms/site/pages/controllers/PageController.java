/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.pages.PageContext;
import com.dynamia.cms.site.pages.PageNotFoundException;
import com.dynamia.cms.site.pages.api.PageTypeExtension;
import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.pages.domain.PageParameter;
import com.dynamia.cms.site.pages.services.PageService;

import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.integration.Containers;
import tools.dynamia.integration.ObjectMatcher;

/**
 *
 * @author mario
 */
@Controller
public class PageController {

	@Autowired
	private PageService service;
	@Autowired
	private SiteService coreService;

	private LoggingService logger = new SLF4JLoggingService(PageController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request) {
		logger.debug("Loading site home page");
		return page("index", request);
	}

	@RequestMapping(value = "/{page}", method = RequestMethod.GET)
	public ModelAndView page(@PathVariable String page, HttpServletRequest request) {

		return getPage(page, request);
	}

	private ModelAndView getPage(String pageAlias, HttpServletRequest request) {

		ModelAndView mv = new ModelAndView("site/page");

		Site site = coreService.getSite(request);

		Page page = loadPage(site, pageAlias, mv);
		configurePageType(page, site, mv, request);

		return mv;
	}

	private Page loadPage(Site site, String pageAlias, ModelAndView mv) {
		Page page = null;
		if (site != null) {
			page = service.loadPage(site, pageAlias);

			if (page == null) {
				logger.debug("Page not found [" + pageAlias + "] in site " + site);
				throw new PageNotFoundException(pageAlias, site.getKey());
			}

			mv.addObject("page", page);
			mv.addObject("pageImage", page.getImageURL());
			mv.addObject("title", page.getTitle());
			mv.addObject("subtitle", page.getSubtitle());
			mv.addObject("icon", page.getIcon());

			if (page.getSummary() != null && !page.getSummary().isEmpty()) {
				mv.addObject("metaDescription", page.getSummary());
			}

			if (page.getTags() != null && !page.getTags().isEmpty()) {
				mv.addObject("metaKeywords", page.getTags());
			}

			if (page.getParameters() != null) {
				Map<String, Object> pageParams = new HashMap<String, Object>();
				for (PageParameter param : page.getParameters()) {
					if (param.isEnabled()) {
						pageParams.put(param.getName(), param.getValue());
					}
				}
				mv.addObject("pageParams", pageParams);
			}

		}
		return page;
	}

	private PageTypeExtension getExtension(final String type) {
		Collection<PageTypeExtension> types = Containers.get().findObjects(PageTypeExtension.class, new ObjectMatcher<PageTypeExtension>() {

			@Override
			public boolean match(PageTypeExtension object) {
				return object.getId().equals(type);
			}
		});

		for (PageTypeExtension pageTypeExtension : types) {
			return pageTypeExtension;
		}

		return null;
	}

	private void configurePageType(Page page, Site site, ModelAndView mv, HttpServletRequest request) {
		if (page != null) {
			String type = page.getType();
			PageTypeExtension pageTypeExt = getExtension(type);
			if (pageTypeExt != null) {
				PageContext context = new PageContext(page, site, mv, request);
				pageTypeExt.setupPage(context);
			}
		}
	}
}
