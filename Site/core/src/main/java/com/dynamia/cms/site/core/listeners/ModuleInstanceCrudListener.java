package com.dynamia.cms.site.core.listeners;

import com.dynamia.cms.site.core.domain.ModuleInstance;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.domain.util.CrudServiceListenerAdapter;
import tools.dynamia.integration.sterotypes.Component;

@Component
public class ModuleInstanceCrudListener extends CrudServiceListenerAdapter<ModuleInstance> {

	@Override
	public void beforeCreate(ModuleInstance entity) {
		checkAlias(entity);
	}

	@Override
	public void beforeUpdate(ModuleInstance entity) {
		checkAlias(entity);
	}

	private void checkAlias(ModuleInstance entity) {
		if (entity.getAlias() == null || entity.getAlias().isEmpty()) {
			entity.setAlias(StringUtils.simplifiedString(entity.getTitle()));
		}

	}

}
