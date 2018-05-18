package tools.dynamia.cms.products

import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.users.IgnoringAntMatcher

@CMSExtension
class StoreIgnoreSecurityMatcher implements IgnoringAntMatcher {

    @Override
    String[] matchers() {
        return ["/store/**"]
    }

}
