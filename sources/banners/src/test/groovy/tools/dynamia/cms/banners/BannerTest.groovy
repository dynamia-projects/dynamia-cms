/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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

package tools.dynamia.cms.banners

import org.junit.Assert
import org.junit.Test
import tools.dynamia.cms.core.CMSUtil

class BannerTest {

	@Test
	void shouldAddThumbnailsPath() {

		String expected = "http://www.dynamiacms.com/resources/products/gallery/thumbnails/abc.jpg?w=100&h=100"
		String input = "http://www.dynamiacms.com/resources/products/gallery/abc.jpg"

		tools.dynamia.cms.banners.domain.Banner banner = new tools.dynamia.cms.banners.domain.Banner()
		banner.setImageURL(input)

		CMSUtil util = new CMSUtil(null)
		String result = util.getThumbnailURL(input, "100", "100")
		System.out.println("INPUT: " + result)
		System.out.println("RESULT: " + result)
		Assert.assertEquals(expected, result)
	}
	
	@Test
	void shouldAddThumbnailsPathWithSpaces() {

		String expected = "http://www.dynamiacms.com/resources/products/gallery/thumbnails/super%20tales%20pascuales.jpg?w=100&h=100"
		String input = "http://www.dynamiacms.com/resources/products/gallery/super%20tales%20pascuales.jpg"

		tools.dynamia.cms.banners.domain.Banner banner = new tools.dynamia.cms.banners.domain.Banner()
		banner.setImageURL(input)

		CMSUtil util = new CMSUtil(null)
		String result = util.getThumbnailURL(input, "100", "100")
		System.out.println("INPUT: " + result)
		System.out.println("RESULT: " + result)
		Assert.assertEquals(expected, result)
	}

}
