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
package tools.dynamia.cms.site.banners.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import tools.dynamia.cms.site.banners.domain.Banner;
import tools.dynamia.cms.site.banners.domain.BannerCategory;
import tools.dynamia.cms.site.banners.services.BannerService;
import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.sterotypes.Service;
import tools.dynamia.io.FileInfo;

@Service
class BannerServiceImpl implements BannerService {

	private static final String CACHE_NAME = "banners";
	@Autowired
	private CrudService crudService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dynamia.cms.site.banners.services.impl.BannerService#
	 * getBannersByCategory (java.lang.Long)
	 */
	@Override
	@Cacheable(value = CACHE_NAME, key = "'banCat'+#categoryId")
	public List<Banner> getBannersByCategory(Long categoryId) {

		QueryParameters qp = QueryParameters.with("category.id", categoryId).add("enabled", true).orderBy("order",
				true);

		return crudService.find(Banner.class, qp);

	}

	@Override
	public List<Banner> createBannersFromCategory(Long categoryId) {
		BannerCategory category = crudService.find(BannerCategory.class, categoryId);
		if (category != null && category.getFolderImages() != null) {
			List<Banner> banners = new ArrayList<>();
			CMSUtil util = new CMSUtil(category.getSite());
			String[] files = util.listFilesNames(category.getFolderImages(), "jpg,png,gif");
			if (files != null) {
				for (String fileName : files) {
					Banner banner = new Banner();
					FileInfo fileInfo = new FileInfo(new File(new File(category.getFolderImages()), fileName));
					banner.setImageURL(CMSUtil.getResourceURL(category.getSite(), fileInfo.getFile()));
					banner.setTitle(fileInfo.getDescription());
					banners.add(banner);
				}
			}
			return banners;
		} else {
			return Collections.EMPTY_LIST;
		}

	}

}
