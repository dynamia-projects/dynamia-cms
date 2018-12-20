/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
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
