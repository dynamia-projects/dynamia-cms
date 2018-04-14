package tools.dynamia.cms.site.products

import tools.dynamia.cms.site.core.api.CMSExtension
import tools.dynamia.cms.site.users.IgnoringAntMatcher

@CMSExtension
class StoreIgnoreSecurityMatcher implements IgnoringAntMatcher {

    @Override
    String[] matchers() {
        return ["/store/**"]
    }

}
