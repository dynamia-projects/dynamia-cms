/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
