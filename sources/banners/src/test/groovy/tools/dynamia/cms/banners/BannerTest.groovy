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
