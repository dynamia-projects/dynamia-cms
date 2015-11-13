package com.dynamia.cms.admin.core;

import tools.dynamia.integration.sterotypes.Component;
import tools.dynamia.viewers.Field;
import tools.dynamia.viewers.FieldCustomizer;

@Component
public class SiteAwareFieldCustomizer implements FieldCustomizer {

	@Override
	public void customize(String view, Field field) {
		if (field.getName().equals("site")) {
			field.setVisible(false);
		}
	}

}
