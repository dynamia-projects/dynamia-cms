package tools.dynamia.cms.site.core;

import org.junit.Test;

import junit.framework.Assert;

public class CMSUtilTest {

	
	@Test
	public void shouldClearJSessionID(){
		String path = "/shopping/cart/add/1143;jsessionid=1FADQWEFASDAq3qASD";
		String expected = "/shopping/cart/add/1143";
		
		String actual = CMSUtil.clearJSESSIONID(path);
		Assert.assertEquals(expected, actual);
	}
}
