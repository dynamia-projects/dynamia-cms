package tools.dynamia.cms.admin.core.zk.actions.global

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.InstallAction
import tools.dynamia.app.template.ApplicationGlobalAction
import tools.dynamia.cms.admin.core.zk.SiteManager

@InstallAction
public class SettingsAction extends ApplicationGlobalAction {

	@Autowired
	private SiteManager siteManager;

	public SettingsAction() {
		setName("Settings");
		setImage("cog");
		setPosition(2);
		setAttribute("background", "btn btn-warning btn-flat bg-yellow ");
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		siteManager.edit();
	}

}
