/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
