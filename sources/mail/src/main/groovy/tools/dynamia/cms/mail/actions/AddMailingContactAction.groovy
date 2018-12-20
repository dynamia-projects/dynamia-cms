/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.mail.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.domain.services.CrudService

@CMSAction
class AddMailingContactAction implements SiteAction {

	@Autowired
	private tools.dynamia.cms.mail.services.MailService service

    @Autowired
	private CrudService crudService

    @Override
    String getName() {
		return "addMailingContact"
    }

	@Override
    void actionPerformed(ActionEvent evt) {
		
		
		tools.dynamia.cms.mail.domain.MailingContact contact = (tools.dynamia.cms.mail.domain.MailingContact) evt.data

        if(contact!=null){
			if(!service.existsMailingContact(contact)){
				try {
                    contact.site = evt.site
                    crudService.save(contact)
                    CMSUtil.addSuccessMessage(contact.emailAddress +" Inscrito exitosamente, recibira nuestras ofertas, productos nuevos y demas en su correo.", evt.redirectAttributes)
                } catch (Exception e) {
					CMSUtil.addErrorMessage("Error al inscribir contacto", evt.redirectAttributes)
                    e.printStackTrace()
                }
			}else{
				CMSUtil.addWarningMessage("La direccion de email ingresada ya se encuentrada inscrita", evt.redirectAttributes)
            }
		
		}

	}

}
