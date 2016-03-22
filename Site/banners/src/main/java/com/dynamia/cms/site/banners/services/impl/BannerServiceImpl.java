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
