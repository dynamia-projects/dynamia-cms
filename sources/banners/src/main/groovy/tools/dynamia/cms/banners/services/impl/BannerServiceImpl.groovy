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
package tools.dynamia.cms.banners.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.sterotypes.Service
import tools.dynamia.io.FileInfo

@Service
class BannerServiceImpl implements tools.dynamia.cms.banners.services.BannerService {

	private static final String CACHE_NAME = "banners"
	@Autowired
	private CrudService crudService

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dynamia.cms.site.banners.services.impl.BannerService#
	 * getBannersByCategory (java.lang.Long)
	 */
	@Override
	@Cacheable(value = BannerServiceImpl.CACHE_NAME, key = "'banCat'+#categoryId")
	List<tools.dynamia.cms.banners.domain.Banner> getBannersByCategory(Long categoryId) {

		QueryParameters qp = QueryParameters.with("category.id", categoryId).add("enabled", true).orderBy("order",
				true)

		return crudService.find(tools.dynamia.cms.banners.domain.Banner.class, qp)

	}

	@Override
	List<tools.dynamia.cms.banners.domain.Banner> createBannersFromCategory(Long categoryId) {
		tools.dynamia.cms.banners.domain.BannerCategory category = crudService.find(tools.dynamia.cms.banners.domain.BannerCategory.class, categoryId)
		if (category != null && category.folderImages != null) {
			List<tools.dynamia.cms.banners.domain.Banner> banners = new ArrayList<>()
			CMSUtil util = new CMSUtil(category.site)
			String[] files = util.listFilesNames(category.folderImages, "jpg,png,gif")
			if (files != null) {
				for (String fileName : files) {
					tools.dynamia.cms.banners.domain.Banner banner = new tools.dynamia.cms.banners.domain.Banner()
					FileInfo fileInfo = new FileInfo(new File(new File(category.folderImages), fileName))
                    banner.imageURL = CMSUtil.getResourceURL(category.site, fileInfo.file)
                    banner.title = fileInfo.description
					banners.add(banner)
				}
			}
			return banners
		} else {
			return Collections.EMPTY_LIST
		}

	}

}