package com.dynamia.cms.admin.core.zk;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

import tools.dynamia.domain.services.CrudService;
import tools.dynamia.domain.util.DomainUtils;
import tools.dynamia.integration.Containers;
import tools.dynamia.zk.crud.ui.EntityPickerBox;

@SuppressWarnings("deprecation")
public class EntityConverter implements TypeConverter, Converter<Object, Object, Component> {

	@Override
	public Object coerceToUi(Object beanProp, Component component, BindContext ctx) {
		return coerceToUi(beanProp, component);
	}

	@Override
	public Object coerceToBean(Object compAttr, Component component, BindContext ctx) {
		return coerceToBean(compAttr, component);
	}

	@Override
	public Object coerceToUi(Object val, Component comp) {
		CrudService crudService = Containers.get().findObject(CrudService.class);

		try {
			if (val != null) {
				EntityPickerBox entityPicker = (EntityPickerBox) comp;
				Long id = new Long(val.toString());
				Object entity = crudService.find(entityPicker.getEntityClass(), id);
				return entity;
			}
		} catch (NumberFormatException e) {			
		}

		return null;
	}

	@Override
	public Object coerceToBean(Object val, Component comp) {
		if (DomainUtils.isJPAEntity(val)) {
			return DomainUtils.getJPAIdValue(val).toString();
		} else {
			return null;
		}
	}

}
