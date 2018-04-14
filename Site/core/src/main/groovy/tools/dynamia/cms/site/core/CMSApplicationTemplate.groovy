package tools.dynamia.cms.site.core

import tools.dynamia.app.template.ApplicationTemplate
import tools.dynamia.app.template.InstallTemplate
import tools.dynamia.app.template.Skin
import tools.dynamia.app.template.TemplateContext

@InstallTemplate
class CMSApplicationTemplate implements ApplicationTemplate {

	@Override
    String getName() {
		return "default"
    }

	@Override
    Map<String, Object> getProperties() {
		return new HashMap<>()
    }

	@Override
    List<Skin> getSkins() {
		return Collections.EMPTY_LIST
    }

	@Override
    Skin getDefaultSkin() {
		return new Skin("", "", "", "")
    }

	@Override
    void init(TemplateContext context) {

	}

}
