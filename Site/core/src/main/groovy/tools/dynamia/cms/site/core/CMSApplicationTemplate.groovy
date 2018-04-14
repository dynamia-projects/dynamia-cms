package tools.dynamia.cms.site.core

import tools.dynamia.app.template.ApplicationTemplate
import tools.dynamia.app.template.InstallTemplate
import tools.dynamia.app.template.Skin
import tools.dynamia.app.template.TemplateContext

@InstallTemplate
public class CMSApplicationTemplate implements ApplicationTemplate {

	@Override
	public String getName() {
		return "default";
	}

	@Override
	public Map<String, Object> getProperties() {
		return new HashMap<>();
	}

	@Override
	public List<Skin> getSkins() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public Skin getDefaultSkin() {
		return new Skin("", "", "", "");
	}

	@Override
	public void init(TemplateContext context) {

	}

}
