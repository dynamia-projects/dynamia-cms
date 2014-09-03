package com.dynamia.cms.site.core.listeners;

import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.tools.commons.StringUtils;
import com.dynamia.tools.domain.util.CrudServiceListenerAdapter;
import com.dynamia.tools.integration.sterotypes.Component;

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
