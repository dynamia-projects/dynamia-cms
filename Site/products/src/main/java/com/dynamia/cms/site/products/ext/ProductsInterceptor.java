/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.ext;

import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.api.SiteRequestInterceptorAdapter;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.ProductsUtil;

/**
 *
 * @author mario
 */
@CMSExtension
public class ProductsInterceptor extends SiteRequestInterceptorAdapter {

	@Override
	protected void afterRequest(Site site, ModelAndView mv) {

		ProductsUtil.setupDefaultVars(site, mv);

	}

}
