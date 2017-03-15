package tools.dynamia.cms.site.banners;

import org.junit.Assert;
import org.junit.Test;

import tools.dynamia.cms.site.banners.domain.Banner;
import tools.dynamia.cms.site.core.CMSUtil;

public class BannerTest {

	@Test
	public void shouldAddThumbnailsPath() {

		String expected = "http://www.dynamiacms.com/resources/products/gallery/thumbnails/abc.jpg?w=100&h=100";
		String input = "http://www.dynamiacms.com/resources/products/gallery/abc.jpg";

		Banner banner = new Banner();
		banner.setImageURL(input);

		CMSUtil util = new CMSUtil(null);
		String result = util.getThumbnailURL(input, "100", "100");
		System.out.println("INPUT: " + result);
		System.out.println("RESULT: " + result);
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void shouldAddThumbnailsPathWithSpaces() {

		String expected = "http://www.dynamiacms.com/resources/products/gallery/thumbnails/super%20tales%20pascuales.jpg?w=100&h=100";
		String input = "http://www.dynamiacms.com/resources/products/gallery/super%20tales%20pascuales.jpg";

		Banner banner = new Banner();
		banner.setImageURL(input);

		CMSUtil util = new CMSUtil(null);
		String result = util.getThumbnailURL(input, "100", "100");
		System.out.println("INPUT: " + result);
		System.out.println("RESULT: " + result);
		Assert.assertEquals(expected, result);
	}

}
