package tools.dynamia.cms.admin.core.zk.actions.global;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.app.template.ApplicationGlobalAction;
import tools.dynamia.cms.admin.core.zk.SiteManager;

@InstallAction
public class ApplyChangedAction extends ApplicationGlobalAction {

	@Autowired
	private SiteManager siteManager;

	public ApplyChangedAction() {
		setName("Apply");
		setDescription("All changes are visible in site");
		setImage("refresh");
		setAttribute("background", "btn btn-primary btn-flat bg-light-blue-active");
		
		
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		siteManager.clearCache();
	}

}
