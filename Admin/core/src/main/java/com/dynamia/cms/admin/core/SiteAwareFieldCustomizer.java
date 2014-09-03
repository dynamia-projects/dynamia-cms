package com.dynamia.cms.admin.core;

import com.dynamia.tools.integration.sterotypes.Component;
import com.dynamia.tools.viewers.Field;
import com.dynamia.tools.viewers.FieldCustomizer;

@Component
public class SiteAwareFieldCustomizer implements FieldCustomizer {

	@Override
	public void customize(String view, Field field) {
		if (field.getName().equals("site")) {
			field.setVisible(false);
		}
	}

}
