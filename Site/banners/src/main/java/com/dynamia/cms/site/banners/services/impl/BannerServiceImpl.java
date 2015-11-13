package com.dynamia.cms.site.banners.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.dynamia.cms.site.banners.domain.Banner;
import com.dynamia.cms.site.banners.services.BannerService;

import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.sterotypes.Service;

@Service
class BannerServiceImpl implements BannerService {

	private static final String CACHE_NAME = "banners";
	@Autowired
	private CrudService crudService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dynamia.cms.site.banners.services.impl.BannerService#getBannersByCategory
	 * (java.lang.Long)
	 */
	@Override
	@Cacheable(value = CACHE_NAME, key = "'banCat'+#categoryId")
	public List<Banner> getBannersByCategory(Long categoryId) {

		QueryParameters qp = QueryParameters.with("category.id", categoryId)
				.add("enabled", true)
				.orderBy("order", true);

		return crudService.find(Banner.class, qp);

	}

}
