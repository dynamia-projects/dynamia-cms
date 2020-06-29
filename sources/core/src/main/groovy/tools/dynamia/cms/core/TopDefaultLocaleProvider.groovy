package tools.dynamia.cms.core

import tools.dynamia.commons.LocaleProvider
import tools.dynamia.integration.sterotypes.Provider

@Provider
class TopDefaultLocaleProvider implements LocaleProvider {
    @Override
    int getPriority() {
        return 0
    }

    @Override
    Locale getDefaultLocale() {
        return Locale.getDefault()
    }
}
