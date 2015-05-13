package com.dynamia.cms.site.users.actions;

import com.dynamia.cms.site.core.api.CMSAction;

@CMSAction
public class UserContactInfoMenuAction implements UserMenuAction {

	@Override
	public String getLabel() {
		return "Direcciones de Contacto";
	}

	@Override
	public String getDescription() {
		return getLabel();
	}

	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public String action() {
		return "users/addresses";
	}

}
