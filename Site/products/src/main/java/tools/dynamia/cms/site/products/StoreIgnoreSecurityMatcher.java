package tools.dynamia.cms.site.products;

import tools.dynamia.cms.site.core.api.CMSExtension;
import tools.dynamia.cms.site.users.IgnoringAntMatcher;

@CMSExtension
public class StoreIgnoreSecurityMatcher implements IgnoringAntMatcher {

	@Override
	public String[] matchers() {
		return new String[] { "/store/**" };
	}

}
